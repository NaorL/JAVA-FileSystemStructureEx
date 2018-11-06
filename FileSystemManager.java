import java.util.*;

public class FileSystemManager {

    /*Data members*/
    private Item m_Root = new Item("Directory", "Root", null, true, 0);

    private static FileSystemManager INSTANCE = null;
    private static Object mutex = new Object();

    // Private constructor suppresses generation of a (public) default constructor
    private FileSystemManager(){}

    public static FileSystemManager getInstance(){
        FileSystemManager result = INSTANCE;
        if(result == null){
            synchronized (mutex){
                result = INSTANCE;
                if(result == null)
                    INSTANCE = result = new FileSystemManager();
            }
        }
        return INSTANCE;
    }

    /*Methods*/
    public void addFile(String parentDirName, String fileName, int fileSize) {
        if (isValidParentDirectory(parentDirName)) {
            if (isItemExists(fileName))
                System.out.println("Error: File name " + fileName + " already in use!");
            else if (fileName.length() == 0)
                System.out.println("Error: File name can not be an empty string");
            else {
                Item newItemToInsert = new Item("File", fileName, parentDirName, false, fileSize);
                addItemToSystem(newItemToInsert);
            }
        } else if (parentDirName.length() == 0)
            System.out.println("Error: Parent directory can not be an empty string");
        else
            System.out.println("Error: No such parent directory: " + parentDirName);
    }

    private boolean isItemExists(String i_ItemName) {

        if (i_ItemName.length() == 0)
            return false;
        else {
            Item res = getItem(i_ItemName);
            if (res != null)
                return true;
            else
                return false;
        }
    }

    private void addItemToSystem(Item i_NewItemToInsert) {

        Item currentLevel = m_Root;
        String itemName = i_NewItemToInsert.getItemName();
        int itemNameLength = itemName.length();

        for (int i = 0; i < itemNameLength; ++i) {
            Item level = (Item) currentLevel.getItemsMap().get(itemName.charAt(i));
            //if item is null or does not exist, put the new one, otherwise keep the previous one
            if(level == null)
                currentLevel.getItemsMap().put(itemName.charAt(i), new Item(i_NewItemToInsert.getItemType(), i_NewItemToInsert.getItemName(), i_NewItemToInsert.getParentDirectoryName(), i_NewItemToInsert.isDirectory(), i_NewItemToInsert.getFileSize()));
            else if(level != null && level.isExist() == false){
                currentLevel.getItemsMap().putIfAbsent(itemName.charAt(i), new Item(i_NewItemToInsert.getItemType(), i_NewItemToInsert.getItemName(), i_NewItemToInsert.getParentDirectoryName(), i_NewItemToInsert.isDirectory(), i_NewItemToInsert.getFileSize()));
                updateCurrentLevelItem(level, i_NewItemToInsert);
            }
            currentLevel = (Item) currentLevel.getItemsMap().get(itemName.charAt(i));
        }

        currentLevel.setIsExist(true);
        updateParentDirectoryMapOfItems(currentLevel);
    }

    private void updateCurrentLevelItem(Item i_CurrentLevel, Item i_NewItemToInsert) {
        //update type (file/directory), name, parent directory, is directory and the data it holds (file or directory).
        //we wont update the map because we want the keep the path for the previous insertions.

        if(i_CurrentLevel.getItemName().compareTo("Root") != 0){
            i_CurrentLevel.setItemType(i_NewItemToInsert.getItemType());
            i_CurrentLevel.setItemName(i_NewItemToInsert.getItemName());
            i_CurrentLevel.setParentDirectoryName(i_NewItemToInsert.getParentDirectoryName());
            i_CurrentLevel.setIsDirectory(i_NewItemToInsert.isDirectory());
            i_CurrentLevel.setDataInfo(i_NewItemToInsert.getDataInfo());
            i_CurrentLevel.setContainedItems(i_NewItemToInsert.getContainedItems());
        }
    }

    private void updateParentDirectoryMapOfItems(Item i_newItemToInsert) {
        String parentDirectoryName = i_newItemToInsert.getParentDirectoryName();
        Item parentItem = getItem(parentDirectoryName);
        if (parentItem.isDirectory())
            parentItem.addItemToParentContainedItemsList(i_newItemToInsert.getItemName(), i_newItemToInsert);
    }

    private Item getItem(String i_ItemName) {
        int parentDirectoryNameLength = i_ItemName.length();
        Item currentLevel = m_Root, res = null;

        if (i_ItemName.compareTo("Root") == 0)
            return m_Root;
        else {
            for (int i = 0; i < parentDirectoryNameLength; ++i) {
                res = (Item) currentLevel.getItemsMap().get(i_ItemName.charAt(i));
                if (res == null)
                    return null;
                currentLevel = (Item) currentLevel.getItemsMap().get(i_ItemName.charAt(i));
            }
        }

        if (res.IsExist())
            return res;
        else
            return null;
    }

    public void addDir(String parentDirName, String dirName) {
        if (isValidParentDirectory(parentDirName)) {
            if (isItemExists(dirName))
                System.out.println("Error: Directory name " + dirName + " already in use!");
            else if (dirName.length() == 0)
                System.out.println("Error: Directory name can not be an empty string");
            else {
                Item newItemToInsert = new Item("Directory", dirName, parentDirName, true, 0);
                addItemToSystem(newItemToInsert);
            }
        } else if (parentDirName.length() == 0)
            System.out.println("Error: Parent directory can not be an empty string");
        else
            System.out.println("Error: No such parent directory: " + parentDirName);
    }

    private boolean isValidParentDirectory(String i_ParentDirName) {

        if (i_ParentDirName.length() == 0)
            return false;

        Item parentDirectoryItem = getItem(i_ParentDirName);
        if (parentDirectoryItem != null && parentDirectoryItem.isDirectory() && parentDirectoryItem.IsExist())
            return true;
        else
            return false;
    }

    public void delete(String name) {
        Item itemToDelete = getItem(name);
        if (name == "Root")
            System.out.println("Error: You have no permission to delete the item from the system!");
        else if (itemToDelete == null)
            System.out.println("Error: Deletion aborted, file name does not exist!");
        else if (itemToDelete.getItemType().compareTo("File") == 0) {
            removeItem(itemToDelete.getItemName());
            removeFromParentDirectoryItemsList(itemToDelete);
        }
        //it's a directory
        else {
            removeFromParentDirectoryItemsList(itemToDelete);
            deleteDirectory(itemToDelete);
            itemToDelete.setIsExist(false);
        }
    }

    private void removeItem(String i_ItemToDelete) {
        Item item = getItem(i_ItemToDelete);
        item.setIsExist(false);
    }

    private void removeFromParentDirectoryItemsList(Item i_ItemToDelete) {
        //remove from parent directory items list
        Item parentOfItemToDelete = getItem(i_ItemToDelete.getParentDirectoryName());
        HashMap<String, Item> parentItems = parentOfItemToDelete.getContainedItems();
        parentItems.remove(i_ItemToDelete.getItemName());
    }

    private void deleteDirectory(Item i_DirectoryToDelete) {
        HashMap<String, Item> innerItems = i_DirectoryToDelete.getContainedItems();

        for (Map.Entry<String, Item> entry : innerItems.entrySet()) {
            removeItem(entry.getValue().getItemName());
            if (entry.getValue().isDirectory())
                deleteDirectory(entry.getValue());
        }
    }

    public void showFileSystem(){
        Item currentLevel = m_Root;
        System.out.println(m_Root.getItemName() + ">");
        System.out.println(m_Root.getDataInfo());
        showFileSystemRec(currentLevel, m_Root.getItemName());
    }

    private void showFileSystemRec(Item i_CurrentLevel, String i_Path) {
        HashMap<String, Item> items = i_CurrentLevel.getContainedItems();
        for (Map.Entry<String, Item> entry : items.entrySet()) {
            System.out.println(i_Path + "\\" + entry.getValue().getItemName() + ">");
            if (!entry.getValue().isDirectory()){
                System.out.println(entry.getValue().getDataInfo());
            }
            else{
                System.out.println(entry.getValue().getDataInfo());
                showFileSystemRec(entry.getValue(), i_Path + "\\" + entry.getValue().getItemName());
            }
        }
    }
}
