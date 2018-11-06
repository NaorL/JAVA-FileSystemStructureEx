import java.util.Date;

public class File extends Information{

    private String m_FileName;
    private int m_FileSize;
    private Date m_CreationDate;

    public File(String i_FileName, int i_FileSize) {
        this.m_FileName = i_FileName;
        m_FileSize = i_FileSize;
        this.m_CreationDate = new Date();
    }

    @Override
    public String toString() {
        String fs;
        return String.format("\tThe name of the file: %s\n" +
                "\tThe size of the file: %d\n" +
                "\tThe creation date is: " + m_CreationDate.toString(), m_FileName, m_FileSize);
    }
}
