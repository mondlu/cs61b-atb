import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.file.*;

public class Gitlet implements Serializable {
    //instance variables
    private Commit pointer;
    private HashMap<String, Commit> branchHeads;
    private Integer totalCommits;
    private String currentBranch;
    
    
    public static void main(String[] args) {
        //if gitlet already initialized, load the saved pointer
        Gitlet myGitlet = new Gitlet();
        File gitlet = new File(".gitlet/");
        if (gitlet.exists()) {
            myGitlet.pointer = (Commit) tryLoadingFile("commitHistory.ser");
            myGitlet.branchHeads = (HashMap<String, Commit>) tryLoadingFile("branchHeads.ser");
            myGitlet.totalCommits = (Integer) tryLoadingFile("totalCommits.ser");
            myGitlet.currentBranch = (String) tryLoadingFile("currentBranch.ser");
        }
        String command = args[0];
        if (myGitlet.pointer == null && !command.equals("init")) {
            System.out.println("Please initiate a Gitlet");
            return;
        }
        switch (command) {
            case "init":
                myGitlet.init();
                break;
            case "add":
                try {
                    String filename = args[1];
                    myGitlet.add(filename);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please add a file.");
                    return;
                }
                break;
            case "commit":
                try {
                    String msg = args[1];
                    if (msg.equals("")) {
                        System.out.println("Please enter a commit message.");
                        return;
                    }
                    myGitlet.commit(msg);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                break;
            case "log":
                myGitlet.log();
                break;
            case "checkout":
                String[] checkoutArgs = Arrays.copyOfRange(args, 1, args.length);
                if (checkoutArgs.length > 2) {
                    System.out.println("Too many inputs to checkout.");
                    return;
                }
                myGitlet.checkout(checkoutArgs);
                break;
            case "rm":
                String filename = args[1];
                myGitlet.remove(filename); 
                break;
            case "branch":
                String inputBranch = args[1];
                myGitlet.branch(inputBranch);
                break;
            case "rm-branch":
                String removeBranch = args[1];
                myGitlet.removeBranch(removeBranch);    
                break;
            case "global-log":
                myGitlet.globalLog();
                break;
            case "find":
                String commitMessage = args[1];
                myGitlet.find(commitMessage);
                break;
            case "status":
                myGitlet.status();
                break;
            case "reset":
                String commitId = args[1];
                myGitlet.reset(commitId);
                break;
            case "merge":
                String branchToMerge = args[1];
                myGitlet.merge(branchToMerge);  
                break;
            case "rebase":
                String branchToMove = args[1];
                myGitlet.rebase(branchToMove);
                break;
	        case "i-rebase":
			    String branchToiMove = args[1];
		        myGitlet.interactiveRebase(branchToiMove);
				break;                  
            default:
                System.out.println("Invalid command.");
                break;                      
        }
    }
    
    //initialize gitlet in the current directory
    public void init() {
        //check if gitlet already exists in the directory
        File gitlet = new File(".gitlet/");
        if (gitlet.exists()) {
            System.out.println("A gitlet version control system already exists in the current directory.");
            return;
        }
        gitlet.mkdir();
        String gitletPath = ".gitlet/";
        //make the sub files and folder of gitlet
        // (1) commitHistory stores a Commit object
        // (2) branchHeads is a hash map: branch name --> commit
        // (3) staged stores a HashSet<String> of file paths that have been staged
        // (4) markedForRemoval stores a HashSet<String> of file paths that are marked for removal
        // (5) copiesOfFiles is a directory stores the files and subfolder that contains files
        // (6) totalCommits is a Integer that gives the total number of commits; count starts from 0,
        //      initially set at -1
        // (7) currentBranch stores a string that indicates the current branch
        // (8) everyCommit is an HashMap: commit number ---> the commit
        // (9) Hashmap of comments to an array list of commits
        File commitHistory = new File(".gitlet/", "commitHistory.ser");
        File branchHeadsSaved = new File(".gitlet/", "branchHeads.ser");
        File staged = new File(".gitlet/", "staged.ser");
        File markedForRemoval = new File(".gitlet/", "markedForRemoval.ser");
        File totalCommitNum = new File(".gitlet/", "totalCommits.ser");
        File savedBranch = new File(".gitlet/", "currentBranch.ser");
        File everyCommit = new File(".gitlet/", "everyCommit.ser");
        File commentMap = new File(".gitlet/", "commentMap.ser");
        
        try {
            commitHistory.createNewFile();
            branchHeadsSaved.createNewFile();
            staged.createNewFile();
            markedForRemoval.createNewFile();
            totalCommitNum.createNewFile();
            savedBranch.createNewFile();
            everyCommit.createNewFile();
            commentMap.createNewFile();
        } catch (IOException e) {
            System.out.println("An IOException occurred while creating .gitlet sub-files");
        }
        
        //load initial objects
        HashSet<String> stagedFiles = new HashSet<String>();
        saveFile(stagedFiles, "staged.ser");
        HashSet<String> markedFiles = new HashSet<String>();
        saveFile(markedFiles, "markedForRemoval.ser");  
        Integer baseNumber = -1;
        saveFile(baseNumber, "totalCommits.ser");
        String initBranch = "master";
        saveFile(initBranch, "currentBranch.ser");
        HashMap<String, ArrayList<Commit>> commentsToCommit = new HashMap<String, ArrayList<Commit>>();
        saveFile(commentsToCommit, "commentMap.ser");
		HashMap<Integer, Commit> allCommitsEver = new HashMap<Integer, Commit>();
		saveFile(allCommitsEver, "everyCommit.ser");
        
        File copiesOfFiles = new File(".gitlet/", "copiesOfFiles/");
        copiesOfFiles.mkdir();
        
        //initial commit
        commit("initial commit");
        
        //initialize with master branch and head pointer
        HashMap<String, Commit> heads = new HashMap<String, Commit>();
        heads.put(initBranch, pointer);
        saveFile(heads, "branchHeads.ser");    
    }
    
    //add file
    //input examples: wug.txt, files/wug.txt, etc
    public void add(String filename) {
        //check if file exists in the given path
        String storagePath = ".gitlet/copiesOfFiles/";
        File checkFile = new File(filename);
        if(!checkFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        
        //check if the file has been modified
        String lastCommit = getCommitFile(filename, pointer.getFiles());
        if (!lastCommit.equals("")) {
            Path pathToWorkingFile = Paths.get(filename);
            Path pathToLastCommit = Paths.get(storagePath+lastCommit);
            boolean same = true;
            try {
                same = compareFiles(pathToWorkingFile, pathToLastCommit);
            } catch (IOException e) {
                System.out.println("IOException while comparing files in add");
            }
            if (same == true) {
                System.out.println("File has not been modified since the last commit.");
                return;
            }
        }
        
        //check if file has been staged for removal
        HashSet<String> marked = (HashSet<String>) tryLoadingFile("markedForRemoval.ser");
        if (marked.contains(filename)) {
            marked.remove(filename);
            saveFile(marked, "markedForRemoval.ser");
            return;
            
        }
        saveFile(marked, "markedForRemoval.ser");
        
        //add to staged.ser
        HashSet<String> stagedFiles = (HashSet<String>) tryLoadingFile("staged.ser");
        stagedFiles.add(filename);
        saveFile(stagedFiles, "staged.ser");
        
    }
    
    //make a commit
    public void commit(String message) {
		//loading things
		HashSet<String> filesToAdd = (HashSet<String>) tryLoadingFile("staged.ser");
		HashMap<Integer, Commit> allCommitsEver = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
		HashMap<String, ArrayList<Commit>> mapOfComments = (HashMap<String, ArrayList<Commit>>) tryLoadingFile("commentMap.ser");
		
        //make timestamp for commit
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss"); 
        Date date = new Date();
        String timestamp = dateFormat.format(date).toString();
        
        //the initial commit
        if (pointer == null) {
			HashSet<String> emptySet = new HashSet<String>();
            Commit commit = new Commit(0, message, timestamp, emptySet, null);
            pointer = commit;
            saveFile(commit, "commitHistory.ser");
            totalCommits = 0;
            saveFile(totalCommits, "totalCommits.ser");
			allCommitsEver.put(0, pointer);
			saveFile(allCommitsEver, "everyCommit.ser");
			return;
            
        //if not the initial commit 
        } else {
            //get the commit number
            int commitNum = totalCommits + 1;
            //Strings in filesToAdd in the format of the working directory file path
            //ex) wug.txt, files/wug.txt
            //HashSet<String> filesToAdd = (HashSet<String>) tryLoadingFile("staged.ser");
            if (filesToAdd.isEmpty()) {
                System.out.println("No changes added to the commit.");
                return;
            }
            HashSet<String> commitFiles = new HashSet<String>();
			HashSet<String> oldCommitFiles = pointer.getFiles();
            String storagePath = ".gitlet/copiesOfFiles/";
			
			if (oldCommitFiles != null) {
				for (String oldfile : oldCommitFiles) {
					String type = oldfile.substring(oldfile.lastIndexOf("."), oldfile.length());
					String noType = oldfile.substring(0, oldfile.lastIndexOf("-"));
					String platonicFile = noType + type;
					if (!filesToAdd.contains(platonicFile)) {
						commitFiles.add(oldfile);
					}
				}
			}	

            for (String f : filesToAdd) {
                //add the staged files to the inherited commit files
                //fileNamed has commit info appended
                //   ex) wug-1.txt, file/wug-1.txt
                String fileNamed = nameCommitFile(f, commitNum);
				
				//look in oldCommitFiles to determine the inherited files for this commit
				if (!commitFiles.contains(fileNamed)) {
					commitFiles.add(fileNamed);
				}
                //copy the content of the file in the working directory to
                //an appropriate nested and named file in copiesOfFiles
                //examples of inputs:
                //in working directory --> wug.txt
                //    storage: .gitlet/copiesOfFiles/wug.txt
                //in subfolder --> files/wug.txt
                //    storage: .gitlet/copiesOfFiles/files/wug.txt
                
                File fileVersion;
                
                //if f is in a subdirectory, make the subdirectories in copiesofFiles
                if (f.contains("/")) {
                    String direcPath = f.substring(0, f.lastIndexOf("/"));
                    File newDirectory = new File(storagePath, direcPath);
                    newDirectory.mkdirs();
                }
                fileVersion = new File(storagePath, fileNamed); 
                try {
                    fileVersion.createNewFile();
                } catch (IOException e) {
                    System.out.println("IOException while creating file in commit");
                }   
                try {
                    Path workingFilePath = Paths.get(f);
                    Path fileVersionPath = Paths.get(storagePath+fileNamed);
                    CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                    Files.copy(workingFilePath, fileVersionPath, copyOptions);
                } catch (UnsupportedOperationException e) {
                    System.out.println("Exception while copying files during commit");
                } catch (IOException e) {
                    System.out.println("IOException during commit");
                } catch (SecurityException e) {
                    System.out.println("Security Exception during commit");
                }               
            }
            //clear staged.ser
            filesToAdd.clear();
            //make new commit
            Commit newCommit = new Commit(commitNum, message, timestamp, commitFiles, pointer);

            //change pointer
            pointer = newCommit;

            //increment total commits number
            totalCommits = commitNum;
            saveFile(totalCommits, "totalCommits.ser");
            
            //set as the new branch head
            branchHeads.put(currentBranch, pointer);
            saveFile(branchHeads, "branchHeads.ser");

            //add to everyCommit
            allCommitsEver.put(totalCommits, pointer);
            
            //map the message to the commit
            if (mapOfComments.containsKey(message)) {
                ArrayList<Commit> commentCommits = mapOfComments.get(message);
                commentCommits.add(pointer);
            } else {
                ArrayList<Commit> commentCommits = new ArrayList<Commit>();
                commentCommits.add(pointer);
                mapOfComments.put(message, commentCommits);
            }
            saveFile(mapOfComments, "commentMap.ser");
                        
            //save stuff
            saveFile(pointer, "commitHistory.ser");
			saveFile(allCommitsEver, "everyCommit.ser");
			saveFile(filesToAdd, "staged.ser");
			saveFile(mapOfComments, "commentMap.ser");
			
        }
    }
    
    //print log
    public void log() {
        Commit temp = pointer;
        while (temp != null) {
            System.out.println("====");
            System.out.println("Commit " + Integer.toString(temp.getCommitNumber()) + ".");
            System.out.println(temp.getDateTime());
            System.out.println(temp.getCommitMessage());
            System.out.println();
            temp = temp.getPrecedingCommit();
        }       
    }
    
    //checkout
    public void checkout(String[] args) {
        //Dangerous Method Check
        Scanner input = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory.");
        System.out.println("Uncommitted changes may be lost.");
        System.out.println("Are you sure you want to continue? (yes/no)");
        String choice = input.nextLine();

        if (!choice.equals("yes")) {
            return;
        }
        //checkout operations
        String branchOrFile = args[0];
        //check if branch first
        if (args.length == 1 && branchHeads.containsKey(branchOrFile)) {
            if (currentBranch.equals(branchOrFile)) {
                System.out.println("No need to checkout the current branch.");
                return;
            } else if (!branchHeads.containsKey(branchOrFile)) {
                System.out.println("File does not exist in the most recent commit, or no such branch exists.");
                return;
            } else {
                //get the files at the head of the branch
                Commit head = branchHeads.get(branchOrFile);
                HashSet<String> filesOfHead = head.getFiles();
                HashSet<String> filesToChange = new HashSet<String>();
                //strip the files
                //ex) wug-1.txt --> wug.txt, file/wug-1.txt --> file/wug.txt, etc
                for (String s : filesOfHead) {
                    String fileType = s.substring(s.lastIndexOf("."), s.length());
                    String nameOnly = s.substring(0, s.lastIndexOf("-"));
                    String platonicFile = nameOnly + fileType;
                    filesToChange.add(platonicFile);
                }
                //change the files in the working directory
                for (String f : filesToChange) {
                    File workingDirectoryFile = new File(f);
                    if (!workingDirectoryFile.exists()) {
                        workingDirectoryFile.mkdirs();
                    }   
                    String lastVersion = getCommitFile(f, filesOfHead);
                    try {
                        Path workingFilePath = Paths.get(f);
                        Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+lastVersion);
                        CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                        Files.copy(fileVersionPath, workingFilePath, copyOptions);
                    } catch (UnsupportedOperationException e) {
                        System.out.println("Exception while copying files during commit");
                    } catch (IOException e) {
                        System.out.println("IOException during commit");
                    } catch (SecurityException e) {
                        System.out.println("Security Exception during commit");
                    }            
                }
                //set the new current branch and pointer
                currentBranch = branchOrFile;
				saveFile(currentBranch, "currentBranch.ser");
                pointer = branchHeads.get(currentBranch);
                saveFile(pointer, "commitHistory.ser");
				return;           
            }
        // if checking out file name    
        } else if (args.length == 1 && !branchHeads.containsKey(branchOrFile)) {
            File workingDirectoryFile = new File(branchOrFile);
            HashSet<String> filesOfHead = pointer.getFiles();
            HashSet<String> filesToCheck = new HashSet<String>();
            for (String f: filesOfHead) {
                String fileType = f.substring(f.lastIndexOf("."), f.length());
                String nameOnly = f.substring(0, f.lastIndexOf("-"));
                String platonicFile = nameOnly + fileType;
                filesToCheck.add(platonicFile);
            }
            if (!filesToCheck.contains(branchOrFile)) {
                System.out.println("File does not exist in the most recent commit, or no such branch exists.");
                return;
            } else {
                if (!workingDirectoryFile.exists()) {
                    workingDirectoryFile.mkdirs();
                }   
                String lastVersion = getCommitFile(branchOrFile, filesOfHead);
                try {
                    Path workingFilePath = Paths.get(branchOrFile);
                    Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+lastVersion);
                    CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                    Files.copy(fileVersionPath, workingFilePath, copyOptions);
                } catch (UnsupportedOperationException e) {
                    System.out.println("Exception while copying files during commit");
                } catch (IOException e) {
                    System.out.println("IOException during commit");
                } catch (SecurityException e) {
                    System.out.println("Security Exception during commit");
                }            
            }
			return;
        //if there are two arguments    
        } else if (args.length == 2) {
            String commitId = args[0];
            String filename = args[1];
            File workingDirectoryFile = new File(filename);
            if (!workingDirectoryFile.exists()) {
                workingDirectoryFile.mkdirs();
            }   
            String lastVersion = nameCommitFile(filename, Integer.parseInt(commitId));
            try {
                Path workingFilePath = Paths.get(filename);
                Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+lastVersion);
                CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                Files.copy(fileVersionPath, workingFilePath, copyOptions);
            } catch (UnsupportedOperationException e) {
                System.out.println("Exception while copying files during commit");
            } catch (IOException e) {
                System.out.println("IOException during commit");
            } catch (SecurityException e) {
                System.out.println("Security Exception during commit");
            }
        }   
    }
    
    //remove
    public void remove(String filename) {
        //look to see if file is in previous commit
        // wug-1.txt wug.txt
        HashSet<String> allFilesToConsider = pointer.getFiles();
        HashSet<String> platonicFiles = new HashSet<String>();
        for (String s : allFilesToConsider) {
            String type = s.substring(s.lastIndexOf("."), s.length());
            String nameOnly = s.substring(0, s.lastIndexOf("-"));
            String platonicName = nameOnly + type;
            platonicFiles.add(platonicName);
        }
        
        if (!platonicFiles.contains(filename)) {
            System.out.println("No reason to remove the file.");
        }
                
        //get the staged files
        HashSet<String> staged = (HashSet<String>) tryLoadingFile("staged.ser");
        if (staged.contains(filename)) {
            staged.remove(filename);
        }
        saveFile(staged, "staged.ser");
        
        HashSet<String> marked = (HashSet<String>) tryLoadingFile("markedForRemoval.ser");
        marked.add(filename);
        saveFile(marked, "markedForRemoval.ser");       
    }
    
    //branch
    public void branch(String inputBranch) {
        if (branchHeads.containsKey(inputBranch)){
            System.out.println("A branch with that name already exists.");
            return;
        }
        branchHeads.put(inputBranch, pointer);
        saveFile(branchHeads, "branchHeads.ser");
    }
    
    //remove branch
    public void removeBranch(String inputBranch) {
        if (currentBranch.equals(inputBranch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        } else if (!branchHeads.containsKey(inputBranch)) {
            System.out.println("A branch with that name does not exist");
        } else {
            branchHeads.remove(inputBranch);
        }
        saveFile(branchHeads, "branchHeads.ser");       
    }
    
    //global log
    public void globalLog() {
        HashMap<Integer, Commit> allTheCommitsEver = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
        for (Integer c : allTheCommitsEver.keySet()) {
            Commit assocCommit = allTheCommitsEver.get(c);
            System.out.println("====");
            System.out.println("Commit " + Integer.toString(assocCommit.getCommitNumber()) + ".");
            System.out.println(assocCommit.getDateTime());
            System.out.println(assocCommit.getCommitMessage());
            System.out.println();
        }       
    }
    
    //find
    public void find(String message) {
        HashMap<String, ArrayList<Commit>> commentToCommit = (HashMap<String, ArrayList<Commit>>) tryLoadingFile("commentMap.ser");
        if (!commentToCommit.containsKey(message)) {
            System.out.println("Found no commit with that message.");
            return;
        }
        ArrayList<Commit> theCommitsWeNeed = commentToCommit.get(message);
        if (theCommitsWeNeed.isEmpty()) {
            System.out.println("Found no commit with that message.");
            return;
        } else {
            for (Commit c : theCommitsWeNeed) {
                System.out.println(c.getCommitNumber());
            }
        }       
    }
    
    //status
    public void status() {
        System.out.println("=== Branches ===");
        System.out.print("*");
        System.out.println(currentBranch);
        Set<String> otherBranches = branchHeads.keySet();
        for (String s : otherBranches) {
            if (!s.equals(currentBranch)) {
                System.out.println(s);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        HashSet<String> filesStaged = (HashSet<String>) tryLoadingFile("staged.ser");
        for (String fs: filesStaged) {
            System.out.println(fs);
        }
        saveFile(filesStaged, "staged.ser");
        System.out.println();
        System.out.println("=== Files Marked For Removal ===");
        HashSet<String> filesMarked = (HashSet<String>) tryLoadingFile("markedForRemoval.ser");
        for (String fm : filesMarked) {
            System.out.println(fm);
        }
        saveFile(filesMarked, "markedForRemoval.ser");
    }
    
    //reset
    public void reset(String commitId) { 
        //Dangerous Method Check
        Scanner input = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory.");
        System.out.println("Uncommitted changes may be lost.");
        System.out.println("Are you sure you want to continue? (yes/no)");
        String choice = input.nextLine();

        if (!choice.equals("yes")) {
            return;
        }
        HashMap<Integer, Commit> allTheCommitsEver = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
        Commit desiredCommit = allTheCommitsEver.get(Integer.parseInt(commitId));
		if (desiredCommit == null) {
			System.out.println("No commit with that id exists");
			return; 
		}
        
        //get the commit files of desired commit
        HashSet<String> commitFiles = desiredCommit.getFiles();
        HashSet<String> filesToChange = new HashSet<String>();
        //strip the files
        //ex) wug-1.txt --> wug.txt, file/wug-1.txt --> file/wug.txt, etc
        for (String s : commitFiles) {
            String fileType = s.substring(s.lastIndexOf("."), s.length());
            String nameOnly = s.substring(0, s.lastIndexOf("-"));
            String platonicFile = nameOnly + fileType;
            filesToChange.add(platonicFile);
        }
        //change the files in the working directory
        for (String f : filesToChange) {
            File workingDirectoryFile = new File(f);
            if (!workingDirectoryFile.exists()) {
                workingDirectoryFile.mkdirs();
            }   
            String lastVersion = getCommitFile(f, commitFiles);
            try {
                Path workingFilePath = Paths.get(f);
                Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+lastVersion);
                CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                Files.copy(fileVersionPath, workingFilePath, copyOptions);
            } catch (UnsupportedOperationException e) {
                System.out.println("Exception while copying files during commit");
            } catch (IOException e) {
                System.out.println("IOException during commit");
            } catch (SecurityException e) {
                System.out.println("Security Exception during commit");
            }                
        }
        branchHeads.put(currentBranch, desiredCommit);
        saveFile(branchHeads, "branchHeads.ser");       
    }
    
    //merge
    public void merge(String givenBranch) {
        //Dangerous Method Check
        Scanner input = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory.");
        System.out.println("Uncommitted changes may be lost.");
        System.out.println("Are you sure you want to continue? (yes/no)");
        String choice = input.nextLine();

        if (!choice.equals("yes")) {
            return;
        }
        if (!branchHeads.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        Commit givenBranchHead = branchHeads.get(givenBranch);
        if (givenBranchHead.getCommitNumber() == pointer.getCommitNumber()) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Commit splitNode = identifySplit(pointer, givenBranchHead);
        HashSet<String> commonFiles = splitNode.getFiles();
        HashSet<String> pointerFiles = pointer.getFiles();
        HashSet<String> givenBranchHeadFiles = givenBranchHead.getFiles();
        HashSet<String> uniqueGivenBranchHeadFiles = uniqueFiles(givenBranchHeadFiles);

        for (String s : uniqueGivenBranchHeadFiles) {
            String storagePath = ".gitlet/copiesOfFiles/";
            //get the version to compare
            String splitVersion = getCommitFile(s, commonFiles);
            String pointerVersion = getCommitFile(s, pointerFiles);
            String givenBranchVersion = getCommitFile(s, givenBranchHeadFiles);
            
            //only do stuff if the givenBranchHead has the file
            if (!givenBranchHead.equals("")) {
                //if the split node does not have the file
                if (splitVersion.equals("")) {
                    //if the pointer does not have the file either
                    if (pointerVersion.equals("")) {
                        File sFile = new File(s);
                        try {
                            if (!sFile.exists()) {
                                sFile.mkdirs();
                            }
                            Path copyFrom = Paths.get(storagePath + givenBranchVersion);
                            Path copyTo = Paths.get(sFile.getPath());
                            CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                            Files.copy(copyFrom, copyTo, copyOptions);
                        } catch (UnsupportedOperationException e) {
                            System.out.println("Exception while copying in merge");
                        } catch (IOException e) {
                            System.out.println("IOException during merge");
                        } catch (SecurityException e) {
                            System.out.println("Security exception during merge");
                        }
                    //if pointer does have a version of the file    
                    } else if (!pointerVersion.equals("")) {
                        //only do stuff if the pointer version is not the same
                        if (!pointerVersion.equals(givenBranchVersion)) {
                            //copy the branch head version into the WD, append .conflicted
                            File sFile = new File(s + ".conflicted");
                            try {
                                sFile.mkdirs();
                                Path copyFrom = Paths.get(storagePath + givenBranchVersion);
                                Path copyTo = Paths.get(sFile.getPath());
                                CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                                Files.copy(copyFrom, copyTo, copyOptions);
                            } catch (UnsupportedOperationException e) {
                                System.out.println("Exception while copying in merge");
                            } catch (IOException e) {
                                System.out.println("IOException during merge");
                            } catch (SecurityException e) {
                                System.out.println("Security Exception during merge");
                            }
                        }
                    }
                //if the split version does exist   
                } else if (!splitVersion.equals("")) {
                    //if the file in the current pointer is unmodified, but is in given branch
                    if (splitVersion.equals(pointerVersion) && !splitVersion.equals(givenBranchVersion)) {
                        //change the WD file to the branch version
                        File sFile = new File(s);
                        try {
                            if (!sFile.exists()) {
                                sFile.mkdirs();
                            }
							System.out.println("third clause" + storagePath + givenBranchVersion);
							System.out.println("destination: " + sFile.getPath());
							System.out.println(sFile.getPath());
                            Path copyFrom = Paths.get(storagePath + givenBranchVersion);
                            Path copyTo = Paths.get(sFile.getPath());
                            CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                            Files.copy(copyFrom, copyTo, copyOptions);
                        } catch (UnsupportedOperationException e) {
                            System.out.println("Exception while copying in merge");
                        } catch (IOException e) {
                            System.out.println("IOException during merge");
                        } catch (SecurityException e) {
                            System.out.println("Security exception during merge");
                        }
                    //if the pointer version is not the same as the branch head version 
                    } else if (!splitVersion.equals(pointerVersion)) {
                        //only do stuff if the pointer version is not the same and the branch version is not the same as split version
                        if (!pointerVersion.equals(givenBranchVersion) && !givenBranchVersion.equals(splitVersion)) {
                            //copy the branch head version into the WD, append .conflicted
                            File sFile = new File(s + ".conflicted");
                            try {
								System.out.println("fourth clause" + storagePath + givenBranchVersion);
								System.out.println("destination: " + sFile.getPath());
                                sFile.mkdirs();
								System.out.println(sFile.getPath());
                                Path copyFrom = Paths.get(storagePath + givenBranchVersion);
                                Path copyTo = Paths.get(sFile.getPath());
                                CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                                Files.copy(copyFrom, copyTo, copyOptions);
                            } catch (UnsupportedOperationException e) {
                                System.out.println("Exception while copying in merge");
                            } catch (IOException e) {
                                System.out.println("IOException during merge");
                            } catch (SecurityException e) {
                                System.out.println("Security Exception during merge");
                            }   
                        }
                    }
                }       
            }
        }
    }   
    
    //rebase
    public void rebase(String givenBranch) {
        //Dangerous Method Check
        Scanner input = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory.");
        System.out.println("Uncommitted changes may be lost.");
        System.out.println("Are you sure you want to continue? (yes/no)");
        String choice = input.nextLine();

        if (!choice.equals("yes")) {
            return;
        }
        //fail cases 
        if (currentBranch.equals(givenBranch)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        if (!branchHeads.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        Commit givenBranchHead = branchHeads.get(givenBranch);
        HashSet<Integer> pointerHistory = tellCommitHistory(pointer);
        if (pointerHistory.contains(givenBranchHead.getCommitNumber())) {
            System.out.println("Already up-to-date.");
            return;
        }
        
        //special case 
        HashSet<Integer> givenBranchHistory = tellCommitHistory(givenBranchHead);
        if (givenBranchHistory.contains(pointer.getCommitNumber())) {
            branchHeads.put(currentBranch, givenBranchHead);
            saveFile(branchHeads, "branchHeads.ser");
            pointer = givenBranchHead;
            saveFile(pointer, "commitHistory.ser");
			
	        //change files in working directory
	        HashSet<String> allNewPointerFiles = pointer.getFiles();
	        for (String file : allNewPointerFiles) {
	            String fileType = file.substring(file.lastIndexOf("."), file.length());
	            String nameOnly = file.substring(0, file.lastIndexOf("-"));
	            String platonicFile = nameOnly + fileType;
	            File workingFile = new File(platonicFile);
	            if (!workingFile.exists()) {
	                workingFile.mkdirs();
	            }
	            try {
	                Path workingFilePath = Paths.get(platonicFile);
	                Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+file);
	                if (!compareFiles(workingFilePath, fileVersionPath)) {
	                    CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
	                    Files.copy(fileVersionPath, workingFilePath, copyOptions);
	                }	       
	            } catch (UnsupportedOperationException e) {
	                System.out.println("Exception while copying files during commit");
	            } catch (IOException e) {
	                System.out.println("IOException during commit");
	            } catch (SecurityException e) {
	                System.out.println("Security Exception during commit");
	            }
				
			}	
            return;
		}
		
		HashMap<Integer, Commit> allTheCommitsEverMade = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
        
        //else, find the split commit
        Commit splitCommit = identifySplit(pointer, givenBranchHead);
        
        //find the commit right after the split in the current branch
        Commit afterSplit = findProceeding(splitCommit, pointer);
        
        //loop to rebase working forward from the pointer
        forwardAppend(afterSplit, givenBranchHead, splitCommit, pointer.getCommitNumber(), givenBranch);
		
		branchHeads.put(currentBranch, allTheCommitsEverMade.get(totalCommits));
        pointer = branchHeads.get(givenBranch);
        saveFile(pointer, "commitHistory.ser");
		saveFile(branchHeads, "branchHeads.ser");
        
        //change files in working directory
        HashSet<String> allNewPointerFiles = pointer.getFiles();
        for (String file : allNewPointerFiles) {
            String fileType = file.substring(file.lastIndexOf("."), file.length());
            String nameOnly = file.substring(0, file.lastIndexOf("-"));
            String platonicFile = nameOnly + fileType;
            File workingFile = new File(platonicFile);
            if (!workingFile.exists()) {
                workingFile.mkdirs();
            }
            try {
                Path workingFilePath = Paths.get(platonicFile);
                Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+file);
                if (!compareFiles(workingFilePath, fileVersionPath)) {
                    CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                    Files.copy(fileVersionPath, workingFilePath, copyOptions);
                }       
            } catch (UnsupportedOperationException e) {
                System.out.println("Exception while copying files during commit");
            } catch (IOException e) {
                System.out.println("IOException during commit");
            } catch (SecurityException e) {
                System.out.println("Security Exception during commit");
            }
            
        }   
    }
    
	public void interactiveRebase(String givenBranch) {
        //Dangerous Method Check
        Scanner input = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory.");
        System.out.println("Uncommitted changes may be lost.");
        System.out.println("Are you sure you want to continue? (yes/no)");
        String choice = input.nextLine();

        if (!choice.equals("yes")) {
            return;
        }
        //fail cases 
        if (currentBranch.equals(givenBranch)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        if (!branchHeads.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        Commit givenBranchHead = branchHeads.get(givenBranch);
        HashSet<Integer> pointerHistory = tellCommitHistory(pointer);
        if (pointerHistory.contains(givenBranchHead.getCommitNumber())) {
            System.out.println("Already up-to-date.");
            return;
        }
        
        //special case 
        HashSet<Integer> givenBranchHistory = tellCommitHistory(givenBranchHead);
        if (givenBranchHistory.contains(pointer.getCommitNumber())) {
            branchHeads.put(currentBranch, givenBranchHead);
            saveFile(branchHeads, "branchHeads.ser");
            pointer = givenBranchHead;
            saveFile(pointer, "commitHistory.ser");
			
	        //change files in working directory
	        HashSet<String> allNewPointerFiles = pointer.getFiles();
	        for (String file : allNewPointerFiles) {
	            String fileType = file.substring(file.lastIndexOf("."), file.length());
	            String nameOnly = file.substring(0, file.lastIndexOf("-"));
	            String platonicFile = nameOnly + fileType;
	            File workingFile = new File(platonicFile);
	            if (!workingFile.exists()) {
	                workingFile.mkdirs();
	            }
	            try {
	                Path workingFilePath = Paths.get(platonicFile);
	                Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+file);
	                if (!compareFiles(workingFilePath, fileVersionPath)) {
	                    CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
	                    Files.copy(fileVersionPath, workingFilePath, copyOptions);
	                }	       
	            } catch (UnsupportedOperationException e) {
	                System.out.println("Exception while copying files during commit");
	            } catch (IOException e) {
	                System.out.println("IOException during commit");
	            } catch (SecurityException e) {
	                System.out.println("Security Exception during commit");
	            }
				
			}	
            return;
		}
		
		HashMap<Integer, Commit> allTheCommitsEverMade = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
        
        //else, find the split commit
        Commit splitCommit = identifySplit(pointer, givenBranchHead);
        
        //find the commit right after the split in the current branch
        Commit afterSplit = findProceeding(splitCommit, pointer);
        
        //loop to rebase working forward from the pointer
        interactiveForwardAppend(afterSplit, givenBranchHead, splitCommit, pointer.getCommitNumber(), givenBranch);
		
		branchHeads.put(currentBranch, allTheCommitsEverMade.get(totalCommits));
        pointer = branchHeads.get(givenBranch);
        saveFile(pointer, "commitHistory.ser");
		saveFile(branchHeads, "branchHeads.ser");
        
        //change files in working directory
        HashSet<String> allNewPointerFiles = pointer.getFiles();
        for (String file : allNewPointerFiles) {
            String fileType = file.substring(file.lastIndexOf("."), file.length());
            String nameOnly = file.substring(0, file.lastIndexOf("-"));
            String platonicFile = nameOnly + fileType;
            File workingFile = new File(platonicFile);
            if (!workingFile.exists()) {
                workingFile.mkdirs();
            }
            try {
                Path workingFilePath = Paths.get(platonicFile);
                Path fileVersionPath = Paths.get(".gitlet/copiesOfFiles/"+file);
                if (!compareFiles(workingFilePath, fileVersionPath)) {
                    CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
                    Files.copy(fileVersionPath, workingFilePath, copyOptions);
                }       
            } catch (UnsupportedOperationException e) {
                System.out.println("Exception while copying files during commit");
            } catch (IOException e) {
                System.out.println("IOException during commit");
            } catch (SecurityException e) {
                System.out.println("Security Exception during commit");
            }
            
        }
	}
    
    
    
            
    
    
    //================== HELPER FUNCTIONS =====================
    //=========================================================

    
    //helper function to load ser files
    //adapted from Sarah Kim's tutorial
    private static Object tryLoadingFile(String filepath) {
        Object myObject = null;
        try {
            FileInputStream fileIn = new FileInputStream(".gitlet/" + filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            myObject = objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException e) {
            String msg = "IOException while loading: " + filepath;
            System.out.println(msg);
        } catch (ClassNotFoundException e) {
            String msg = "ClassNotFoundException while loading: " + filepath;
            System.out.println(msg);
        }
        return myObject;
    }
    
    //helper function to save file
    //adpated from Sarah Kim's tutorial
    private static void saveFile(Object myObject, String filepath) {
        String path = ".gitlet/" + filepath;
        if (myObject == null) {
            System.out.println("No object given to save.");
            return;
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(myObject);
            fileOut.close();
            objectOut.close();
        } catch (IOException e) {
            String msg = "IOException while saving to: " + filepath;
            System.out.println(msg);
        }
    }
    
    
    //helper function to name commit files
    //takes in the file path and the commit number
    //ex) (wug.txt, 1) ---> wug-1.txt
    //    (files/wug.txt, 1) ---> files/wug-1.txt
    private static String nameCommitFile(String filepath, int commitNum) {
        String commitNumStr = Integer.toString(commitNum);
        //strip string of file type
        String fileType = filepath.substring(filepath.lastIndexOf("."), filepath.length());
        String noType = filepath.substring(0, filepath.lastIndexOf("."));
        //append the commit identifier
        String commitId = "-" + commitNumStr;
        String finalFile = noType + commitId + fileType;
        return finalFile;
    }
    
    //helper function to compare two files
    //borrowed from http://stackoverflow.com/questions/27379059/determine-if-two-files-store-the-same-content
    //false if not the same
    private static boolean compareFiles(Path f1, Path f2) throws IOException {
        byte[] f1Bytes = Files.readAllBytes(f1);
        byte[] f2Bytes = Files.readAllBytes(f2);
        return Arrays.equals(f1Bytes, f2Bytes);
    }
    
    //helper function called in checkout to get the filepath to latest version in the branch head
    private static String getCommitFile(String file, HashSet<String> fileList) {
        if (fileList == null) {
            String returnString = "";
            return returnString;
        }
        HashMap<String, Integer> latestVersion = new HashMap<String, Integer>();
        //make platonic file name map to the latest version contained in the 
        //given file list
        for (String f : fileList) {
            //example: files/wug-1.txt, files/wug-2.txt
            //    --------> files/wug.txt maps to 2
            String fileType = f.substring(f.lastIndexOf("."), f.length());
            String noType = f.substring(0, f.lastIndexOf("-"));
            String versionStr = f.substring(f.lastIndexOf("-") + 1, f.lastIndexOf("."));
            String fileOnly = noType + fileType;
            int version = Integer.parseInt(versionStr);
            if (!latestVersion.containsKey(fileOnly)) {
                latestVersion.put(fileOnly, version);
            } else if (version > latestVersion.get(fileOnly)) {
                latestVersion.put(fileOnly, version);
            }
        }
        //return a string with the path to the latest file version contained in the commit head
        if (!latestVersion.containsKey(file)) {
            String returnString = "";
            return returnString;
        } else {    
            return nameCommitFile(file, latestVersion.get(file));
        }   
    }
    
    //helper function to filter oldCommits of any files that are marked for removal
    private static HashSet<String> filter(HashSet<String> set) {
        HashSet<String> marked = (HashSet<String>) tryLoadingFile("markedForRemoval.ser");
        if (marked.isEmpty()) {
            saveFile(marked, "markedForRemoval.ser");
            return set;
        } else {
            HashSet<String> returnSet = new HashSet<String>();
            for (String s : set) {
                String type = s.substring(s.lastIndexOf("."), s.length());
                String nameOnly = s.substring(0, s.lastIndexOf("-"));
                String platonicName = nameOnly + type;
                if (!marked.contains(platonicName)) {
                    returnSet.add(s);
                }
            }
            marked.clear();
            saveFile(marked, "markedForRemoval.ser");
            return returnSet;
        }
    }
    
    //helper function to identify the Commit from which two branches split
    private static Commit identifySplit(Commit c1, Commit c2) {
        HashSet<Integer> c1History = new HashSet<Integer>();
        Commit tempc1 = c1;
        while (tempc1 != null) {
            c1History.add(tempc1.getCommitNumber());
            tempc1= tempc1.getPrecedingCommit();
        }
        
        Commit tempc2 = c2;
        while (!c1History.contains(tempc2.getCommitNumber())) {
            tempc2 = tempc2.getPrecedingCommit();
        }
        return tempc2;
    }
    //helper to get commits in a commit's history
    private static HashSet<Integer> tellCommitHistory(Commit givenCommit) {
        HashSet<Integer> returnSet = new HashSet<Integer>();
        Commit temp = givenCommit;
        while (temp != null) {
            returnSet.add(temp.getCommitNumber());
            temp = temp.getPrecedingCommit();
        }
        return returnSet;
    }
    
    //helper funtion to given the unique files in a set of file versions
    private static HashSet<String> uniqueFiles(HashSet<String> set) {
        HashSet<String> returnSet = new HashSet<String>();
        for (String s : set) {
            String type = s.substring(s.lastIndexOf("."), s.length());
            String nameOnly = s.substring(0, s.lastIndexOf("-"));
            String platonicFile = nameOnly + type;
            returnSet.add(platonicFile);    
        }
        return returnSet;
    }
    
	
	private void interactiveForwardAppend(Commit currentP, Commit otherH, Commit split, int endNum, String givenBranch) {
		Scanner input = new Scanner(System.in);
		System.out.println("Currently Replaying: ");
		System.out.println("Commit " + currentP.getCommitNumber() + ".");
		System.out.println(currentP.getDateTime());
		System.out.println(currentP.getCommitMessage());
		System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
		boolean shallWeDoThis = false;
		while (!shallWeDoThis) {
			String choice = input.nextLine();
		    switch (choice) {
			    case "c":
    		    	shallWeDoThis = true;
	                Commit newCommit = copyCommit(currentP, otherH, split, "");
				    branchHeads.put(givenBranch, newCommit);
				    saveFile(branchHeads, "branchHeads");
	                HashMap<Integer, Commit> allTheCommitsEver = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
				    allTheCommitsEver.put(newCommit.getCommitNumber(), newCommit);
				    saveFile(allTheCommitsEver, "everyCommit.ser");
				    if (currentP.getCommitNumber() == endNum) {
					    break;
				    } else {
					    Commit nextC = findProceeding(currentP, pointer);
				        interactiveForwardAppend(nextC, newCommit, split, endNum, givenBranch);
			 	    }
					break;
			    case "s":
			        if (currentP.getCommitNumber() == endNum || currentP.getPrecedingCommit().getCommitNumber() == split.getCommitNumber()) {
			         	System.out.println("Cannot skip the first or the last commit.");
						System.out.println("Would you like to (c)ontinue, or change this comimt's (m)essage?");
			        } else {
					    shallWeDoThis = true;
					    Commit nextC = findProceeding(currentP, pointer);
			    	    interactiveForwardAppend(nextC, otherH, split, endNum, givenBranch);
			        }
					break;
	 	        case "m":
	    		    shallWeDoThis = true;
				    boolean shallWeDoThisMessage = false;
				    System.out.println("Please enter a new message for this commit.");
				    while (!shallWeDoThisMessage) {
					    String newMessage = input.nextLine();
					    if (newMessage.equals("")) {
						    System.out.println("Message cannot be blank.");
       			 	    } else {
				    	    shallWeDoThisMessage = true;
		                    Commit newCommit2 = copyCommit(currentP, otherH, split, newMessage);
				    	    branchHeads.put(givenBranch, newCommit2);
					        saveFile(branchHeads, "branchHeads");
		                    HashMap<Integer, Commit> allTheCommitsEver2 = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
					        allTheCommitsEver2.put(newCommit2.getCommitNumber(), newCommit2);
					        saveFile(allTheCommitsEver2, "everyCommit.ser");
				        }
				    }
					break;	
			    default: 
     			    System.out.println("Wrong input. Please put (c)ontinue, (s)kip, or change (m)essage.");
					break;
			}
		}
	}	
	
	
	
	
    //helper function used in rebase
    private void forwardAppend(Commit currentP, Commit otherH, Commit split, int endNum, String givenBranch) {
        if (currentP.getCommitNumber() == endNum) {
            Commit newCommit = copyCommit(currentP, otherH, split, "");
			branchHeads.put(givenBranch, newCommit);
			saveFile(branchHeads, "branchHeads");
            HashMap<Integer, Commit> allTheCommitsEver = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
			allTheCommitsEver.put(newCommit.getCommitNumber(), newCommit);
			saveFile(allTheCommitsEver, "everyCommit.ser");
			
        } else {
            Commit newCommit = copyCommit(currentP, otherH, split, "");
			branchHeads.put(givenBranch, newCommit);
			saveFile(branchHeads, "branchHeads.ser"); 
            Commit nextC = findProceeding(currentP, pointer);
            HashMap<Integer, Commit> allTheCommitsEver = (HashMap<Integer, Commit>) tryLoadingFile("everyCommit.ser");
			allTheCommitsEver.put(newCommit.getCommitNumber(), newCommit);
			saveFile(allTheCommitsEver, "everyCommit.ser");
            forwardAppend(nextC, newCommit, split, endNum, givenBranch);  
        }
    }
    //findProceeding(splitCommit, pointerHistory);
    private Commit findProceeding(Commit c, Commit p) {
        Commit temp = p;
		while (temp.getPrecedingCommit().getCommitNumber() != c.getCommitNumber()) {
			temp = temp.getPrecedingCommit();
		}
		return temp;
    }
    
    //helper function to copy commits
    private Commit copyCommit(Commit toCopy, Commit headOtherBranch, Commit splitCommit, String interactiveMessage) {
        //make timestamp for commit
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss"); 
        Date date = new Date();
        String timestamp = dateFormat.format(date).toString();
        
        
		//some variables
        String message = "";
		int commitNumber = totalCommits + 1;
		if (interactiveMessage.equals("")) {
			message = toCopy.getCommitMessage();
		}
        if (!interactiveMessage.equals("")) {
			message = interactiveMessage;
		}	
        
        //stuff to use
        HashSet<String> filesOfOtherHead = headOtherBranch.getFiles();
        HashSet<String> filesOfToCopy = toCopy.getFiles();
        HashSet<String> filesOfSplitCommit = splitCommit.getFiles();
        
        //copy the commit files
        //check against all the files of the other head
        HashSet<String> newCopyFiles = new HashSet<String>();
        for (String s : filesOfToCopy) {
            String type = s.substring(s.lastIndexOf("."), s.length());
            String nameOnly = s.substring(0, s.lastIndexOf("-"));
            String platonicFile = nameOnly + type;
            String copyVersion = getCommitFile(platonicFile, filesOfToCopy);
            String otherHeadVersion = getCommitFile(platonicFile, filesOfOtherHead);
			String splitVersion = getCommitFile(platonicFile, filesOfSplitCommit);
			
            String result = compareTwoFiles(copyVersion, otherHeadVersion);
			String compareToSplit = compareTwoFiles(copyVersion, splitVersion);
			
            if (compareToSplit.equals("same")) {
            	if (!result.equals("same")) {
            		newCopyFiles.add(otherHeadVersion);
            	} else {
            		newCopyFiles.add(copyVersion);
            	}
            } else if (!compareToSplit.equals("same") && !result.equals("same")) {
            	newCopyFiles.add(copyVersion);
            } else {
            	newCopyFiles.add(copyVersion);
            }
        }  
		//adding files only added in the given branch
		for (String s : filesOfOtherHead) {
			String type = s.substring(s.lastIndexOf("."), s.length());
            String nameOnly = s.substring(0, s.lastIndexOf("-"));
            String platonicFile = nameOnly + type;
            String copyVersion = getCommitFile(platonicFile, filesOfToCopy);
            String result = compareTwoFiles(copyVersion, s);
            if (result.equals("first doesn't exist")) {
                newCopyFiles.add(s);
            }
        }   
        
        Commit theNewCommit = new Commit(commitNumber, message, timestamp, newCopyFiles, headOtherBranch);
        totalCommits = commitNumber;
        saveFile(totalCommits, "totalCommits.ser");
        return theNewCommit;
                
    }
    
    //helper to helper function that checks which file is the later version
    private static String compareTwoFiles(String f1, String f2) {
        if (f1.equals("")) {
            String returnString = "first doesn't exist";
            return returnString;
        }
        if (f2.equals("")) {
            String returnString = "second doesn't exist";
            return returnString;
        }
        String nameOnly1 = f1.substring(0, f1.lastIndexOf("-"));
        String nameOnly2 = f1.substring(0, f1.lastIndexOf("-"));
        
        if (!nameOnly1.equals(nameOnly2)) {
            String returnString = "";
            return returnString;
        }
        Integer v1 = Integer.parseInt(f1.substring(f1.lastIndexOf("-") + 1, f1.lastIndexOf(".")));
        Integer v2 = Integer.parseInt(f2.substring(f2.lastIndexOf("-") + 1, f2.lastIndexOf(".")));
        if (v1 > v2) {
            String returnString = "first";
            return returnString;
        } else if (v1 == v2) {
            String returnString = "same";
            return returnString;
        } else {
            String returnString = "second";
            return returnString;
        }
    }   
}