
package osProject;

class memoryManager {
    public final int TOTAL_MEMORY = 2048;
    private int availableMemory;

    public memoryManager() {
        this.availableMemory = TOTAL_MEMORY;
    }

    public synchronized boolean allocate(int size) {
        if (availableMemory >= size) {
            availableMemory -= size;
            System.out.println(
                    "Allocated Memory:" + size + " MB | Available Memory" + availableMemory + " MB / " + TOTAL_MEMORY);
            return true;
        }
        System.out.println(
                "Failed to allocate" + size + " MB | Available Memory" + availableMemory + " MB / " + TOTAL_MEMORY);

        return false;
    }

    public synchronized void deallocate(int size) {
        availableMemory += size;
        System.out
                .println("Free Memory:" + size + " MB | Available Memory" + availableMemory + " MB / " + TOTAL_MEMORY);
    }

    public synchronized int getAvailable() {
        return availableMemory;
    }
}
