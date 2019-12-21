package data;
import java.nio.file.Path;

public interface FileHandler {

    public enum Status {
        /**
         * the file is still being handled, not error yet
         */
        HANDLING_NO_ERROR,
        /**
         * the file is still being handled, an error already occurred
         */
        HANDLING_WITH_ERROR,
        /**
         * the file handling was done successfully
         */
        HANDLED_WITH_SUCCESS,
        /**
         * the file handling was done with error(s) 
         */
        HANDLED_WITH_ERROR,
        /**
         * the file count not be handled
         */
        FAILED_TO_HANDLED
    }

    public void handleCreation(final Path inputfile);

    public void handleDeletion(final Path inputfile);
    
    public Path getOutputFile(final Path file);
    
    public Path getReportFile(final Path file);
    
    public boolean outputFileMustBeRegenerated(final Path file);
}
