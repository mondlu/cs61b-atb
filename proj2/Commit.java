import java.util.HashSet;
import java.io.Serializable;
import java.util.HashMap;

public class Commit implements Serializable{
    private int commitNumber;
    private String commitMessage;
    private String dateTime;
    private HashSet<String> associatedFiles;
    private Commit precedingCommit;
    
    public Commit(int cn, String cm, String dt, HashSet<String> af, Commit preCom) {
        commitNumber = cn;
        commitMessage = cm;
        dateTime = dt;
        associatedFiles = af;
        precedingCommit = preCom;
    }
    
    public int getCommitNumber() {
        return commitNumber;
    }
    
    public String getCommitMessage() {
        return commitMessage;
    }
    
    public String getDateTime() {
        return dateTime;
    }

    public Commit getPrecedingCommit() {
        return precedingCommit;
    }
    
    public HashSet<String> getFiles() {
        return associatedFiles;
    }           
}

