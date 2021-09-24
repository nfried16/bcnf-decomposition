import java.util.*;

public class TableReader {
    String[] relation;
    String[][] fdKeys;
    String[][] fdVals;

    public void read() {
        Scanner sc = new Scanner(System.in);

        String relationString = sc.nextLine();
        relation = readRelation(relationString);

        List<String[]> fdKeysList = new ArrayList<>();
        List<String[]> fdValsList = new ArrayList<>();
        while(sc.hasNextLine()) {
            String fd = sc.nextLine();
            if(fd.length() < 4) {
                break;
            }
            String[] keyArr = readFDKeys(fd);
            String[] valArr = readFDVals(fd);
            fdKeysList.add(keyArr);
            fdValsList.add(valArr);
        }
        fdListsToArrs(fdKeysList, fdValsList);
    }

    public String[] readRelation(String relationString) {
        relationString = relationString.replaceAll("\\{", "");
        relationString = relationString.replaceAll("}", "");
        relationString = relationString.replaceAll(" ", "");
        return relationString.split(",");
    }

    public String[] readFDKeys(String fd) {
        fd = fd.replaceAll(" ", "");
        int arrowInd = fd.indexOf('-');
        if(arrowInd == -1) {
            throw new IllegalArgumentException("Invalid FDs");
        }
        String key = fd.substring(0, arrowInd);
        String[] keyArr = key.split(",");
        return keyArr;
    }

    public String[] readFDVals(String fd) {
        fd = fd.replaceAll(" ", "");
        int arrowInd = fd.indexOf('-');
        if(arrowInd == -1) {
            throw new IllegalArgumentException("Invalid FDs");
        }
        String val = fd.substring(arrowInd+2);
        String[] valArr = val.split(",");
        return valArr;
    }

    public void fdListsToArrs(List<String[]> fdKeysList, List<String[]> fdValsList) {
        fdKeys = new String[fdKeysList.size()][0];
        fdVals = new String[fdValsList.size()][0];
        for(int i = 0; i < fdKeysList.size(); i++) {
            fdKeys[i] = fdKeysList.get(i);
            fdVals[i] = fdValsList.get(i);
        }
    }
}
