
import java.util.HashMap;

public class TNode {
    private String attribute;
    private HashMap<String, TNode> children;
    private boolean leaf;

    public TNode(String attribute, boolean leaf) {
        this.attribute = attribute;
        this.leaf = leaf;
        children = new HashMap<>();
    }
    public void addChild(String attribute, TNode child) {
        children.put(attribute, child);
    }
    public String getAttribute() {
        return attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    public HashMap<String, TNode> getChildren() {
        return children;
    }
    public void setChildren(HashMap<String, TNode> children) {
        this.children = children;
    }
    public boolean isLeaf() {
        return leaf;
    }
}
