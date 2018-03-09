package main.java.diffparser.parser;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Optional;

public class LineInterval {
    int begin;
    int end;

    public LineInterval(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public static LineInterval convert(Optional<Position> begin, Optional<Position> end) {
        if (begin == null || end == null ||
                !begin.isPresent() || !end.isPresent()) {
            return null;
        }

        return new LineInterval(begin.get().line, end.get().line);
    }

    public static LineInterval convert(MethodDeclaration methodDeclaration) {
        return convert(methodDeclaration.getBegin(), methodDeclaration.getEnd());
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
