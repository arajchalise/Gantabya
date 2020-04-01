package com.gantabya.project;

import java.util.*;

class Vertex{
    public String label;
    public boolean wasVisited;

    public Vertex(String lab){
        label = lab;
        wasVisited = false;
    }
}


class Graph{
    private final int MAX_VERTS = 95;
    private Vertex vertex[];
    private int adjMatrix[][];
    private int sld[];
    private int nVerts;
    private Stack<Integer> s;
    private int[] stationTraversed = new int[95];
    private int i=1;
    public Graph(){
        vertex = new Vertex[MAX_VERTS];
        adjMatrix = new int[MAX_VERTS][MAX_VERTS];
        sld = new int[MAX_VERTS];
        nVerts = 1;
        s = new Stack<Integer>();
    }

    public void addVertex(String i){
        vertex[nVerts++] = new Vertex(i);
    }

    public void addEdge(int u, int v){
        adjMatrix[u][v] = 1;
        adjMatrix[v][u] = 1;
    }


    public int getAdjUnvisitedVertex(int v){
        for (int j = 0; j < nVerts; j++){
            if (adjMatrix[v][j] == 1 && vertex[j].wasVisited == false){
                return j;
            }
        }
        return -1;
    }

    public int[] dfs(int u, int w){
        vertex[u].wasVisited = true;
        s.push(u);
        while (!s.isEmpty()){
            int v = getAdjUnvisitedVertex(s.peek());
            if (v == -1){
                s.pop();
            } else {
                if (v == w){
                    vertex[v].wasVisited = true;
                    break;
                }
                vertex[v].wasVisited = true;
                s.push(v);
            }
        }
        s.push(w);
        while (!s.isEmpty()){
            stationTraversed[i++] = s.peek();
            s.pop();
        }
        return stationTraversed;
    }

}

public class Graphs {
    public static void main(String[] args) {

    }
}
