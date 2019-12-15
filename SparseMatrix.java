/*
butinai elem < zeros
 */
package sparsematrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author K
 */
public class SparseMatrix {

    /**
     * @param args the command line arguments
     * matrixL 500
     * pasisk  100
     */
    int matrixLength = 500;
    int pasiskirstymas = 100;
    int masyvoDydis = matrixLength * matrixLength / pasiskirstymas;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        SparseMatrix obj = new SparseMatrix();
        String zeros = "0";

        //Sudedami duomenys
        List<String> zodziai = gautiZodzius();
        String[][] sparseMatrix = obj.sudetiIMatrica(zeros, zodziai);
        obj.spausdintiMatrica(sparseMatrix);

        //Sudaroma sparse matricos        
        CompactMatrixElement[] compactMatrix = obj.sudarytiSparseMatrica(sparseMatrix, zeros);
        List<CompactMatrixElement> listMatrix = obj.sudarytiListMatrica(sparseMatrix, zeros);

        //Spausidnama matrica, sudeta i lista
        spausdintiMatricaMasyve(compactMatrix);
        spausdintiMatricaListe(listMatrix);

        //----------------------------------------------------------------
        //Testuojama greitaveika
        //----------------------------------------------------------------
        int ieskomuElementuSk = compactMatrix.length;

        long paprMatricosTrukme = obj.paprGreitaveika(zodziai, ieskomuElementuSk, sparseMatrix);
        long sparceMasyveTrukme = sparceGreitaveikaMasyve(zodziai, ieskomuElementuSk, compactMatrix);
        long sparceListeTrukme = sparceGreitaveikaListe(zodziai, ieskomuElementuSk, listMatrix);

        System.out.println("Paieškos laikas paprastojoje matricoje (nanosekundėmis): ");
        System.out.println(paprMatricosTrukme);
        System.out.println("Paieškos laikas Sparce Matricoje masyve (nanosekundėmis): ");
        System.out.println(sparceMasyveTrukme);
        System.out.println("Paieškos laikas Sparce Matricoje liste (nanosekundėmis): ");
        System.out.println(sparceListeTrukme);

    }
    
    static long sparceGreitaveikaListe(List<String> zodziai, int elSk, List<CompactMatrixElement> matrix) {
        long startTime = System.nanoTime();
        ieskotiSparceListe(zodziai, elSk, matrix);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
    
    static void ieskotiSparceListe(List<String> zodziai, int elSk, List<CompactMatrixElement> matrix) {

        System.out.println("Testojama Sparce matricos liste greitaveika");
        System.out.println("----------------------------------------");

        for (int i = 0; i < elSk; i++) {
            String ieskomasZodis = zodziai.get(i);

            for (int j = 0; j < matrix.size(); j++) {
                if (ieskomasZodis == matrix.get(j).duom) {
                    System.out.println("Rasta: " + i + " " + matrix.get(j).duom);
                }
            }
        }

        System.out.println("-----------Testavimas baigtas-----------");
        System.out.println("----------------------------------------");
    }

    static long sparceGreitaveikaMasyve(List<String> zodziai, int elSk, CompactMatrixElement[] compactMatrix) {
        long startTime = System.nanoTime();
        ieskotiSparce(zodziai, elSk, compactMatrix);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    static long paprGreitaveika(List<String> zodziai, int elSk, String[][] sparseMatrix) {
        SparseMatrix obj = new SparseMatrix();
        
        long startTime = System.nanoTime();
        obj.ieskotiPaprastojeMatricoje(zodziai, elSk, sparseMatrix);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    static void ieskotiSparce(List<String> zodziai, int elSk, CompactMatrixElement[] compactMatrix) {

        System.out.println("Testojama Sparce matricos masyve greitaveika");
        System.out.println("----------------------------------------");

        for (int i = 0; i < elSk; i++) {
            String ieskomasZodis = zodziai.get(i);

            for (int j = 0; j < compactMatrix.length; j++) {
                if (ieskomasZodis == compactMatrix[j].duom) {
                    System.out.println("Rasta: " + i + " " + compactMatrix[j].duom);
                }
            }
        }

        System.out.println("-----------Testavimas baigtas-----------");
        System.out.println("----------------------------------------");
    }

    void ieskotiPaprastojeMatricoje(List<String> zodziai, int elSk, String[][] sparseMatrix) {
        System.out.println("----------------------------------------");
        System.out.println("Testojama paprastos matricos greitaveika");

        for (int i = 0; i < elSk; i++) {
            String ieskomasZodis = zodziai.get(i);

            for (int j = 0; j < matrixLength; j++) {
                for (int k = 0; k < matrixLength; k++) {
                    if (ieskomasZodis == sparseMatrix[j][k]) {
                        System.out.println("Rasta: " + i + " " + sparseMatrix[j][k]);
                    } else {
//                        System.out.println();
                    }
                }
            }
        }

        System.out.println("-----------Testavimas baigtas-----------");
        System.out.println("----------------------------------------");
    }
    
    static void spausdintiMatricaListe(List<CompactMatrixElement> matrix) {
        System.out.println("----Spausdinamas matricos list--------");
        int i = 0;
        for (CompactMatrixElement e : matrix) {
            System.out.println(i + " " + e);
            i++;
        }
        
        System.out.println("Viso elementų sparse list matricoje: " + matrix.size());
        System.out.println("---------------------------------------");
    }

    static void spausdintiMatricaMasyve(CompactMatrixElement[] matrix) {
        System.out.println("----Spausdinamas matricos masyvas--------");
        int i = 0;
        for (CompactMatrixElement e : matrix) {
            System.out.println(i + " " + e);
            i++;
        }
        System.out.println("Viso elementų sparse masyvo matricoje: " + matrix.length);
        System.out.println("---------------------------------------");
    }
    
    List<CompactMatrixElement> sudarytiListMatrica(String[][] sparseMatrix, String zeros) {
        List<CompactMatrixElement> listMatrix = new LinkedList<CompactMatrixElement>();
        
        for (int i = 0; i < matrixLength; i++) {
            for (int j = 0; j < matrixLength; j++) {
                if (sparseMatrix[i][j] != "0") {
                    CompactMatrixElement newElement = new CompactMatrixElement(i, j, sparseMatrix[i][j]);
                    listMatrix.add(newElement);
                }
            }
        }
        
        return listMatrix;
    }

    CompactMatrixElement[] sudarytiSparseMatrica(String[][] sparseMatrix, String zeros) {
        CompactMatrixElement[] compactMatrix = new CompactMatrixElement[masyvoDydis];

        int f = 0;

        for (int i = 0; i < matrixLength; i++) {
            for (int j = 0; j < matrixLength; j++) {
                if (sparseMatrix[i][j] != "0") {
                    CompactMatrixElement newElement = new CompactMatrixElement(i, j, sparseMatrix[i][j]);
                    compactMatrix[f] = newElement;
                    f++;
//                    System.out.println("ideta" + "  " + sparseMatrix[i][j]);
                }
            }
        }

        return compactMatrix;
    }

    void spausdintiMatrica(String[][] sparseMatrix) {
        for (int i = 0; i < matrixLength; i++) {
            String eil = "";
            for (int j = 0; j < matrixLength; j++) {
                eil += "[" + i + "]" + "[" + j + "]" + sparseMatrix[i][j] + " ";
            }

            System.out.println(eil);
        }
        System.out.println("--------------------------------------");
        System.out.println("------Duomenys sudeti i matrica-------");
        System.out.println("--------------------------------------");
    }

    String[][] sudetiIMatrica(String zeros, List<String> zodziai) {
        String[][] sparseMatrix = new String[matrixLength][matrixLength];

        int elementas = 0;
        int kurisZodis = 0;

        for (int i = 0; i < matrixLength; i++) {
            for (int j = 0; j < matrixLength; j++) {
                if (elementas % pasiskirstymas == 0) {
                    sparseMatrix[i][j] = zodziai.get(kurisZodis);
                    kurisZodis++;
                } else {
                    sparseMatrix[i][j] = zeros;
                }
                elementas++;
            }
        }

        return sparseMatrix;
    }

    static List<String> gautiZodzius() throws FileNotFoundException, IOException {
        File file = new File("zodynas.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> zodziai = new LinkedList<String>();

        String st;
        while ((st = br.readLine()) != null) {
            zodziai.add(st);
        }

        System.out.println("--------------------------------------");
        System.out.println("sudeti zodziai i LinkedList:");
        for (String zodis : zodziai) {
            System.out.println(zodis);
        }
        System.out.println("--------------------------------------");
        return zodziai;
    }

    public class CompactMatrixElement {

        private int x;
        private int y;
        private String duom;

        public CompactMatrixElement() {
        }

        public CompactMatrixElement(int x, int y, String duom) {
            this.x = x;
            this.y = y;
            this.duom = duom;
        }

        @Override
        public String toString() {
            return "elementas: " + "[" + x + "]" + "[" + y + "]" + duom;
        }

        //---------------------------------------------------------------------
        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setDuom(String duom) {
            this.duom = duom;
        }

        //---------------------------------------------------------------------
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getDuom() {
            return duom;
        }
    }
}
