package com.nxllxn.plantuml.writer;

import com.nxllxn.plantuml.java.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.nxllxn.plantuml.utils.AssembleUtil.extraEmptyLine;
import static com.nxllxn.plantuml.utils.AssembleUtil.startNewLine;

/**
 *
 * @author wenchao
 */
public class PrimitivePlantUmlCodeFileWriter extends AbstarctWriter {
    private String fileName;

    private Logger logger = LoggerFactory.getLogger(PrimitivePlantUmlCodeFileWriter.class);

    public PrimitivePlantUmlCodeFileWriter(String fileName, List<CompilationUnit> compilationUnits) {
        super(compilationUnits);

        this.fileName = fileName;
    }

    @Override
    public void write() {
        StringBuilder fileContentBuilder = new StringBuilder();

        fileContentBuilder.append("@startuml");

        startNewLine(fileContentBuilder);

        if (compilationUnits != null){
            compilationUnits.forEach(compilationUnit -> {
                fileContentBuilder.append(compilationUnit.getFormattedContent());

                extraEmptyLine(fileContentBuilder);
            });
        }

        fileContentBuilder.append("@enduml");

        try {
            File writeToFile = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(writeToFile);
            fileOutputStream.write(fileContentBuilder.toString().getBytes());
        } catch (IOException e){
            logger.error("Unable to write to file {}",fileName);
        }
    }
}
