import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Directory extends Information{

    private String m_DirectoryName;
    private Date m_CreationDate;

    public Directory(String i_DirectoryName) {
        this.m_DirectoryName = i_DirectoryName;
        this.m_CreationDate = new Date();
    }

    @Override
    public String toString() {
        return String.format("\tThe name of the directory: %s\n" +
                "\tThe creation date is: " + m_CreationDate.toString(), m_DirectoryName);
    }

}
