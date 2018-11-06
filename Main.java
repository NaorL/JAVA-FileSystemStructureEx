public class Main {

    public static void main(String[] args) {

        FileSystemManager fsm = FileSystemManager.getInstance();

        fsm.addDir("Root", "C");

        fsm.addDir("Root", "D");

        fsm.addDir("C", "Program files");

        fsm.addFile("Program files", "intellij", 1024);

        fsm.addFile("D", "Spotinst ex", 256);

        fsm.addDir("D", "Programs");

        fsm.addFile("Programs", "Word", 128);

        fsm.addFile("Programs", "PowerPoint", 56);

        fsm.showFileSystem();

        fsm.delete("Programs");

        System.out.println("After deletion:");

        fsm.showFileSystem();

    }
}
