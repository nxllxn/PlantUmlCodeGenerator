package com.nxllxn.plantuml.writer;

import com.nxllxn.plantuml.java.CompilationUnit;

import java.util.List;

public abstract class AbstarctWriter implements Writer{
    protected List<CompilationUnit> compilationUnits;

    public AbstarctWriter(List<CompilationUnit> compilationUnits) {
        this.compilationUnits = compilationUnits;
    }
}
