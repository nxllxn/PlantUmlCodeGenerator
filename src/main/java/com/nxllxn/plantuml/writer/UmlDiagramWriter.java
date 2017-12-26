package com.nxllxn.plantuml.writer;

import com.nxllxn.plantuml.java.CompilationUnit;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static com.nxllxn.plantuml.utils.AssembleUtil.extraEmptyLine;
import static com.nxllxn.plantuml.utils.AssembleUtil.startNewLine;

public class UmlDiagramWriter extends AbstarctWriter {
    private String fileName;

    private String fileType;
    public static final String FILE_TYPE_SVG = "svg";
    public static final String FILE_TYPE_PNG = "png";

    private Logger logger = LoggerFactory.getLogger(UmlDiagramWriter.class);

    public UmlDiagramWriter(String fileName,String fileType, List<CompilationUnit> compilationUnits) {
        super(compilationUnits);

        this.fileName = fileName;
        this.fileType = fileType;
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
            File writeToFile = new File(fileName + "." + fileType);

            SourceStringReader sourceStringReader = new SourceStringReader(fileContentBuilder.toString());
            if(fileType.equals(FILE_TYPE_PNG)){
                sourceStringReader.generateImage(writeToFile);
            } else {
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                sourceStringReader.generateImage(os, new FileFormatOption(FileFormat.SVG));
                os.close();
                final String svgFileString = new String(os.toByteArray(), Charset.forName("UTF-8"));

                FileOutputStream fileOutputStream = new FileOutputStream(writeToFile);
                fileOutputStream.write(svgFileString.getBytes());
                fileOutputStream.close();
            }
        } catch (IOException e){
            logger.error("Unable to write to file {}",fileName);
        }
    }
}
