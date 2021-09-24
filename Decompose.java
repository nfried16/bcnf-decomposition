import java.util.*;

public class Decompose {
    Map<String, Set<String>> fds;
    Map<String, Set<String>> valToKeys;

    public static void main(String[] args) {
        TableReader t = new TableReader();
        t.read();
        String[] relation = t.relation;

        String[][] fdKeys = t.fdKeys;
        String[][] fdVals = t.fdVals;
//        String[][] fdKeys = {{"A", "B"}, {"C"}};
//        String[][] fdVals = {{"C"}, {"B", "D"}};
//        fdKeys[i]->fdVals[i] i.e A,B->C and C->B,D

        Decompose d = new Decompose();
        String[][] bcnfTables = d.decompose(relation, fdKeys, fdVals);
    }

    public String[][] decompose(String[] relation, String[][] fdKeys, String[][] fdVals) {
        this.fds = new HashMap<>();
        this.valToKeys = new HashMap<>();
        // Validate input
        validate(relation, fdKeys, fdVals);
        // Add empty sets to both maps
        for(String attr: relation) {
            fds.put(attr, new HashSet<>());
            fds.get(attr).add(attr);
            valToKeys.put(attr, new HashSet<>());
            valToKeys.get(attr).add(attr);
        }
        // Add all FDs to the maps
        for(int i = 0; i < fdKeys.length; i++) {
            addToMap(fdKeys[i], fdVals[i]);
        }
        // Apply BCNF
        String[][] bcnf = applyBCNF(relation);
        return bcnf;
    }

    public String[][] applyBCNF(String[] relation) {
        // Return tables
        List<Set<String>> tables = new ArrayList<>();
        // Original table
        Set<String> original = new HashSet<>(Arrays.asList(relation));
        // DFS
        Stack<Set<String>> stack = new Stack<>();
        stack.add(original);
        stackloop:
        while(!stack.isEmpty()) {
            Set<String> table = stack.pop();
            // Check every attr in table
            for(String attr: table) {
                Set<String> implies = fds.get(attr);
                Set<String> intersect = new HashSet<>();
                intersect.addAll(implies);
                intersect.retainAll(table);
                // Check if this value attr has any non-bcnf fds
                if(intersect.size() == 1) {
                    continue;
                }
                else if(intersect.size() == table.size()) {
                    continue;
                }
                else {
                    Set<String> table2 = new HashSet<>(table);
                    table2.removeAll(intersect);
                    table2.add(attr);
                    // Add two decomposed tables to stack
                    stack.add(intersect);
                    stack.add(table2);
                    // Print this decomposition
                    printDecomp(table, attr, fds.get(attr), intersect, table2);
                    continue stackloop;
                }
            }
            tables.add(table);
        }
        // Print final bcnf
        printBCNF(tables);
        // Convert to array
        String[][] ret = new String[tables.size()][0];
        for(int i = 0; i < tables.size(); i++) {
            Set<String> table = tables.get(i);
            String[] tableArr = table.toArray(new String[table.size()]);
            ret[i] = tableArr;
        }
        return ret;
    }

    public void addToMap(String[] keys, String[] values) {
        Stack<String> stack = new Stack<>();
        Set<String> seen = new HashSet<>();
        for(String key: keys) {
            stack.add(key);
            seen.add(key);
        }
        // Go DFS on these FDs ahaha
        while(!stack.isEmpty()) {
            String addVals = stack.pop();
            for(String val: values) {
                // KEY => VALUES
                fds.get(addVals).add(val);
                // VALUE <= KEYS
                valToKeys.get(val).add(addVals);
            }
            // Now, all vars that imply this one imply all the values too
            // Add to stack
            for(String implies: valToKeys.get(addVals)) {
                if(!seen.contains(implies)) {
                    stack.add(implies);
                    seen.add(implies);
                }
            }
        }
    }

    public void printDecomp(Set<String> orig, String key, Set<String> vals, Set<String> t1, Set<String> t2) {
        System.out.print("Decompose: ");
        printTable(orig);
        System.out.print(" by ");
        System.out.print(key + "->");
        StringBuilder deps = new StringBuilder("");
        for(String v: vals) {
            if(!v.equals(key)) {
                deps.append(v);
                deps.append(",");
            }
        }
        deps.deleteCharAt(deps.length()-1);
        System.out.print(deps.toString());
        System.out.print(" into ");
        printTable(t1);
        System.out.print(" and ");
        printTable(t2);
        System.out.println();
    }

    public void printTable(Set<String> table) {
        StringBuilder arr = new StringBuilder("{");
        for(String attr: table) {
            arr.append(attr);
            arr.append(",");
        }
        arr.deleteCharAt(arr.length()-1);
        arr.append("}");
        System.out.print(arr.toString());
    }

    public void printBCNF(List<Set<String>> tables) {
        System.out.print("BCNF: ");
        int i = 0;
        for(Set<String> table: tables) {
            i++;
            printTable(table);
            if(i != tables.size()) {
                System.out.print(", ");
            }
        }
    }

    public boolean validate(String[] relation, String[][] fdKeys, String[][] fdVals) {
        Set<String> validAttr = new HashSet<>();
        for(String attr: relation) {
            validAttr.add(attr);
        }
        for(int i = 0; i < fdKeys.length; i++) {
            String[] keys = fdKeys[i];
            String[] vals = fdVals[i];
            for(String key: keys) {
                if(!validAttr.contains(key)) {
                    throw new IllegalArgumentException ("Invalid relations and/or FDs");
                }
            }
            for(String val: vals) {
                if(!validAttr.contains(val)) {
                    throw new IllegalArgumentException ("Invalid relations and/or FDs");
                }
            }
        }
        return true;
    }
}
