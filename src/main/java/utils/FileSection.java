package utils;

import java.io.File;

public record FileSection(File file,
                          long offset,
                          long length) {
    //
}
