package cc.iostream.algos;

/**
 * Sequence Set.
 */
public class SeqSet {
    private static class Node {
        private int start, end;
        private int boundLeft, boundRight;
        private Node left, right, parent;
        private SeqSet tree;

        public Node(int value, Node parent, SeqSet tree) {
            this.start = this.end = value;
            this.boundLeft = this.boundRight = value;
            this.parent = parent;
            this.tree = tree;
        }

        public void insert(int value) {
            if (value == start - 1) {
                start = value;

                // We're growing to the left; check with the left child
                if (left != null && left.boundRight == start - 1) {
                    // OK so we've got a nice block here, but we still need to find the closest node
                    final Node closest = left.highest();

                    // Collate
                    closest.removeFromParent();
                    start = closest.start;
                    closest.parent.updateBoundsUpwards();
                }
                return;
            }
            else if (value == end + 1) {
                end = value;

                if (right != null && right.boundLeft == end + 1) {
                    // OK it's another block here
                    final Node closest = right.lowest();

                    // Collate
                    closest.removeFromParent();
                    end = closest.end;
                    closest.parent.updateBoundsUpwards();
                }
                return;
            }
            else if (value < start) {
                if (left == null) {
                    left = new Node(value, this, tree);
                }
                else {
                    left.insert(value);
                }
            }
            else if (value > end) {
                if (right == null) {
                    right = new Node(value, this, tree);
                }
                else {
                    right.insert(value);
                }
            }
            else {
                return;
            }

            balance();
        }

        private void removeFromParent() {
            if (left != null && right != null) {
                throw new IllegalArgumentException("Must be a leaf or half-leaf");
            }

            if (parent.left == this) {
                if (left != null) {
                    parent.left = left;
                    left.parent = parent;
                }
                else if (right != null) {
                    parent.left = right;
                    right.parent = parent;
                }
                else {
                    parent.left = null;
                }
            }
            else if (parent.right == this) {
                if (left != null) {
                    parent.right = left;
                    left.parent = parent;
                }
                else if (right != null) {
                    parent.right = right;
                    right.parent = parent;
                }
                else {
                    parent.right = null;
                }
            }
        }

        private void balance() {
            final int boundMiddlePoint = boundLeft + (boundRight - boundLeft) / 2;

            if (boundMiddlePoint > end) {
                // Sloping right
                rotateRight();
            }
            else if (boundMiddlePoint < start) {
                // Sloping left
                rotateLeft();
            }

        }

        private void rotateRight() {
            if (right == null) {
                throw new IllegalArgumentException("Cannot rotate right when right is null");
            }

            final Node oldLeft = right.left;
            final Node newRoot = right;

            if (this.parent != null) {
                if (this.parent.right == this) {
                    this.parent.right = newRoot;
                }
                else if (this.parent.left == this) {
                    this.parent.left = newRoot;
                }
                else {
                    throw new IllegalArgumentException("Invalid state");
                }
            }
            else {
                tree.root = newRoot;
            }

            newRoot.parent = this.parent;
            newRoot.linkLeft(this);
            this.linkRight(oldLeft);
            this.updateBounds();
            parent.updateBounds();
        }

        private void rotateLeft() {
            if (left == null) {
                throw new IllegalArgumentException("Cannot rotate left when left is null");
            }

            final Node oldRight = left.right;
            final Node newRoot = left;

            if (this.parent != null) {
                if (this.parent.right == this) {
                    this.parent.right = newRoot;
                }
                else if (this.parent.left == this) {
                    this.parent.left = newRoot;
                }
                else {
                    throw new IllegalArgumentException("Invalid state");
                }
            }
            else {
                tree.root = newRoot;
            }

            newRoot.parent = this.parent;
            newRoot.linkRight(this);
            this.linkLeft(oldRight);
            this.updateBounds();
            parent.updateBounds();
        }

        private void linkLeft(Node leftNode) {
            this.left = leftNode;
            if (leftNode != null) {
                leftNode.parent = this;
            }
        }

        private void linkRight(Node rightNode) {
            this.right = rightNode;
            if (rightNode != null) {
                rightNode.parent = this;
            }
        }

        private void updateBounds() {
            boundLeft = Math.min(left != null ? left.boundLeft : start, start);
            boundRight = Math.max(right != null ? right.boundRight : end, end);
        }

        private void updateBoundsUpwards() {
            updateBounds();
            if (parent != null) {
                parent.updateBoundsUpwards();
            }
        }

        public Node highest() {
            if (right != null) {
                return right.highest();
            }
            else {
                return this;
            }
        }

        public Node lowest() {
            if (left != null) {
                return left.lowest();
            }
            else {
                return this;
            }
        }

        public int allocate() {
            if (start > 0) {
                return start - 1;
            }
            else {
                return end + 1;
            }
        }

        public int size() {
            return 1 + (left != null ? left.size() : 0) + (right != null ? right.size() : 0);
        }

        public int height() {
            return Math.max(left != null ? left.height() : 0, right != null ? right.height() : 0) + 1;
        }

        public String toString() {
            return String.format("{\"interval\": \"%d..%d\", \"bound\": \"%d..%d\", \"left\": %s, \"right\": %s}",
                    start,
                    end,
                    boundLeft,
                    boundRight,
                    left != null ? left.toString() : "null",
                    right != null ? right.toString() : "null");
        }
    }

    private Node root;

    public void insert(int value) {
        if (root == null) {
            root = new Node(value, null, this);
        }
        else {
            root.insert(value);
        }
    }

    public int next() {
        if (root == null) {
            root = new Node(0, null, this);
            return 0;
        }
        else {
            return root.allocate();
        }
    }

    public int take() {
        int val = next();
        insert(val);
        return val;
    }

    /**
     * Returns the number of *nodes* in the tree. Not the number of numbers.
     */
    public int size() {
        if (root == null) {
            return 0;
        }
        else {
            return root.size();
        }
    }

    public int height() {
        if (root == null) {
            return 0;
        }
        else {
            return root.height();
        }
    }

    public String toString() {
        return root.toString();
    }
}
