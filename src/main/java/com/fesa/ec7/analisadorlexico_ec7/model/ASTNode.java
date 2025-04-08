package com.fesa.ec7.analisadorlexico_ec7.model;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private String value;
    private List<ASTNode> children;

    public ASTNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }
    
    public String toString() {
        return toStringHelper(0);
    }
    
    private String toStringHelper(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(level)).append(value).append("\n");
        for (ASTNode child : children) {
            sb.append(child.toStringHelper(level + 1));
        }
        return sb.toString();
    }
}