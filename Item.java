import java.util.HashMap;

public class Item {

    //Could be "Directory" or "File"
    private String m_ItemType;
    //Same as the contained object, File.FileName or Directoy.DirectoryName
    private String m_ItemName;
    //True when creating the file/directory, else false
    private boolean m_IsExist;
    private String m_ParentDirectoryName;
    private boolean m_IsDirectory;
    private int m_FileSize;

    //Holds the actual object, either file or directory
    private Information m_DataInfo;

    //private List<String> m_ContainedItems = new ArrayList<String>();
    private HashMap<String, Item> m_ContainedItems = new HashMap<>();
    //private List<String> m_InnerFilesAndSubDirectories = new ArrayList<>();
    private HashMap m_ItemsMap = new HashMap<Character, Item>();

    /*Ctor*/
    public Item(String i_ItemType, String i_ItemName, String i_ParentDirectoryName, boolean i_IsDirectory, int i_FileSize) {
        this.m_ItemType = i_ItemType;
        this.m_ItemName = i_ItemName;
        this.m_ParentDirectoryName = i_ParentDirectoryName;
        this.m_IsDirectory = i_IsDirectory;
        this.m_FileSize = i_FileSize;
        if(m_ItemType.compareTo("File") == 0)
            m_DataInfo = new File(i_ItemName, i_FileSize);
        else
            m_DataInfo = new Directory(i_ItemName);

        if( i_ItemName.compareTo("Root") == 0 )
            m_IsExist = true;
    }

    /*Getters*/
    public HashMap getItemsMap() {
        return m_ItemsMap;
    }

    public boolean IsExist() {
        return m_IsExist;
    }

    public Information getDataInfo() {
        return m_DataInfo;
    }

    public String getItemType() {
        return m_ItemType;
    }

    public String getItemName() {
        return m_ItemName;
    }

    public String getParentDirectoryName() {
        return m_ParentDirectoryName;
    }

    public boolean isDirectory() {
        return m_IsDirectory;
    }

    public int getFileSize() {
        return m_FileSize;
    }

    public HashMap<String, Item> getContainedItems() {
        return m_ContainedItems;
    }

    public boolean isExist() {
        return m_IsExist;
    }

    /*Setters*/

    public void setIsExist(boolean m_IsExist) {
        this.m_IsExist = m_IsExist;
    }

    public void setParentDirectoryName(String i_ParentDirectoryName) {
        this.m_ParentDirectoryName = i_ParentDirectoryName;
    }

    public void setItemType(String i_ItemType) {
        this.m_ItemType = i_ItemType;
    }

    public void setItemName(String i_ItemName) {
        this.m_ItemName = i_ItemName;
    }

    public void setIsDirectory(boolean i_IsDirectory) {
        this.m_IsDirectory = i_IsDirectory;
    }

    public void setDataInfo(Information i_DataInfo) {
        this.m_DataInfo = i_DataInfo;
    }

    public void setContainedItems(HashMap<String, Item> i_ContainedItems) {
        this.m_ContainedItems = i_ContainedItems;
    }

    /*Methods*/
    public void addItemToParentContainedItemsList(String i_ItemName, Item i_ItemToAdd){
        m_ContainedItems.put(i_ItemName, i_ItemToAdd);
    }
}
