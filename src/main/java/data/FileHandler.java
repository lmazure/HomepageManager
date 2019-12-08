package data;
import java.nio.file.Path;

public interface FileHandler {

    public enum Status {
        /**
         * the file handling was done successfully
         */
        HANDLED_WITH_SUCCESS,
        /**
         * the file handling was done with error(s) 
         */
        HANDLED_WITH_ERROR,
        /**
         * the file count not be handler
         */
        FAILED_TO_HANDLED
    }

    public Status handleCreation(final Path inputfile);

    public Status handleDeletion(final Path inputfile);
    
    public Path getOutputFile(final Path file);
    
    public Path getReportFile(final Path file);
    
    public boolean outputFileMustBeRegenerated(final Path file);
}
