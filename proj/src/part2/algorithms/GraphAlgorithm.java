package part2.algorithms;

import org.jetbrains.annotations.NotNull;
import part2.component.*;
import java.util.*;

public class GraphAlgorithm {

    private MatrixAsGraph m_Graph;
    private Queue<VertexInMatrixAsGraph> m_Queue;
    private List<VertexInMatrixAsGraph> m_ListWithOnesVertices;

    public GraphAlgorithm(){
        m_Queue = new LinkedList<>();
        m_Graph = null;
        m_ListWithOnesVertices = null;
    }

    /**
     * Ex-1 FindAllComponents
     * @param i_Matrix
     * @return Collection<HashSet<VertexInMatrixAsGraph>> All Components
     */
    @NotNull
    public Collection<HashSet<VertexInMatrixAsGraph>> Ex1FindAllComponents(int[][] i_Matrix){

        m_Graph = new MatrixAsGraph(i_Matrix);

        return FindAllComponents();

    }

    /**
     * Ex-1 FindAllComponents
     * @return Collection<HashSet<VertexInMatrixAsGraph>> final List Of Components
     */
    @NotNull
    private Collection<HashSet<VertexInMatrixAsGraph>> FindAllComponents(){

        m_ListWithOnesVertices = ((List<VertexInMatrixAsGraph>) m_Graph.GetAllVertex()); // List of 1's
        Collection<HashSet<VertexInMatrixAsGraph>> finalListOfComponents = new LinkedHashSet<>();

        while (!m_ListWithOnesVertices.isEmpty()){//while all list of 1's is not empty

            //take first index of 1's list and find its component
            HashSet<VertexInMatrixAsGraph> neighborsOfSpecificallyVertex = new LinkedHashSet<>(FindComponent(m_ListWithOnesVertices.get(0)));

            finalListOfComponents.add(neighborsOfSpecificallyVertex);//Add found component to list

            for (VertexInMatrixAsGraph Vertex : neighborsOfSpecificallyVertex) {//remove all indexes of found component
                //if after removing there are more indexes that means that there are more components that needs to be found
                m_ListWithOnesVertices.remove(Vertex);
            }
        }

        return finalListOfComponents;
    }

    /**
     * Ex-1 FindComponent
     * Algorithm-BFS
     * @param i_Vertex
     * @return Collection<VertexInMatrixAsGraph> component
     */
    @NotNull
    private Collection<VertexInMatrixAsGraph> FindComponent(VertexInMatrixAsGraph i_Vertex){

        Collection<VertexInMatrixAsGraph> component = new ArrayList<>();
        component.add(i_Vertex);

        m_Queue.add(i_Vertex);
        while(!m_Queue.isEmpty()) {//Traverse on all adjacent indexes

            VertexInMatrixAsGraph currentVertex = m_Queue.remove();

            for (VertexInMatrixAsGraph neighborsOfCurrentVertex : m_Graph.GetAllReachables(currentVertex)) {//get all reachables from index

                if(!component.contains(neighborsOfCurrentVertex)) {//if Index not in list add it, and add to queue to traverse on new index
                    component.add(neighborsOfCurrentVertex);
                    m_Queue.add(neighborsOfCurrentVertex);
                }
            }
        }

        return component;
    }

    /**
     * Ex-2 FindAllShortestRoutes
     * @param i_Matrix
     * @param i_StartVertex
     * @param i_EndVertex
     * @return Set<Collection<VertexInMatrixAsGraph>> allShortestRoutes
     */
    @NotNull
    public Set<Collection<VertexInMatrixAsGraph>> Ex2FindAllShortestRoutes(int[][] i_Matrix, VertexInMatrixAsGraph i_StartVertex, VertexInMatrixAsGraph i_EndVertex){

        m_Graph = new MatrixAsGraph(i_Matrix);

        return FindAllShortestRoutes(i_StartVertex, i_EndVertex);

    }

    /**
     * Ex-2 check all routes
     * @param i_StartVertex
     * @param i_EndVertex
     * @return HashSet<Collection<VertexInMatrixAsGraph>> allShortestRoutes
     */
    @NotNull
    private HashSet<Collection<VertexInMatrixAsGraph>> FindAllShortestRoutes(VertexInMatrixAsGraph i_StartVertex, VertexInMatrixAsGraph i_EndVertex){

        HashSet<Collection<VertexInMatrixAsGraph>> allShortestRoutes = new HashSet<>();

        if(m_Graph.GetMatrixAsGraphSize() > 25000 ) { //check if matrix > 50x50
            return allShortestRoutes;
        }

        Collection<VertexInMatrixAsGraph> bfsTree = FindComponent(i_StartVertex);//Find tree(Via BFS algo)

        if(!bfsTree.contains(i_EndVertex)){//check if end_vertex in BFS tree
            return allShortestRoutes;
        }

        Collection<VertexInMatrixAsGraph> bestShortRoute = findShortRoute(i_EndVertex, bfsTree);
        allShortestRoutes.add(bestShortRoute);

        Collection<VertexInMatrixAsGraph> allCircleVertex = getAllCircleVertex(i_StartVertex, i_EndVertex);

        if(allCircleVertex.size() > 0){
            //check another routes
            allShortestRoutes.addAll(checkAnotherShortestRoutes(i_StartVertex, i_EndVertex, bestShortRoute.size(), allCircleVertex));
        }

        return allShortestRoutes;
    }

    /**
     * Ex-2 check all routes and return optimally routes
     * @param i_StartVertex
     * @param i_EndVertex
     * @param sizeOfBestShortRoute
     * @param allCircleVertex
     * @return HashSet<Collection<VertexInMatrixAsGraph>> allShortestRoutes
     */
    @NotNull
    private HashSet<Collection<VertexInMatrixAsGraph>> checkAnotherShortestRoutes(VertexInMatrixAsGraph i_StartVertex, VertexInMatrixAsGraph i_EndVertex, int sizeOfBestShortRoute, Collection<VertexInMatrixAsGraph> allCircleVertex) {

        Collection<VertexInMatrixAsGraph> bfsTree;
        Collection<VertexInMatrixAsGraph> routes;
        HashSet<Collection<VertexInMatrixAsGraph>> allShortestRoutes = new HashSet<>();

        //check another routes
        for (var i1 : allCircleVertex) {

            for (var i2: allCircleVertex) {

                if(i1.equals(i2) || i2.equals(i_StartVertex) ||  i2.equals(i_EndVertex)){
                    continue;
                }
                // if is not start or end vertex
                m_Graph.SetValue(i2,0); // Set 0 in this vertex ,Close it and try to find another route

                bfsTree = FindComponent(i_StartVertex);

                if(!bfsTree.contains(i_EndVertex)){ // if there is no route set 1

                    m_Graph.SetValue(i2,1);
                    continue;
                }
                else if(bfsTree.contains(i_EndVertex)){ // if there is a route you will find it, check its size and they will optimally add to the list

                    routes = findShortRoute(i_EndVertex, bfsTree);
                    if(sizeOfBestShortRoute == routes.size()) { //Check if it is the right size
                        allShortestRoutes.add(routes);
                    }
                }
            }

            for (var i2: allCircleVertex) { // set all vertex 1

                m_Graph.SetValue(i2,1);
            }
        }

        return allShortestRoutes;
    }

    /**
     * Ex-2 Returns all circuits
     * @param i_StartVertex
     * @param i_EndVertex
     * @return Collection<VertexInMatrixAsGraph> allCircleIndex
     */
    @NotNull
    private Collection<VertexInMatrixAsGraph> getAllCircleVertex(VertexInMatrixAsGraph i_StartVertex, VertexInMatrixAsGraph i_EndVertex) {

        Collection<VertexInMatrixAsGraph> bfsTree;
        Collection<VertexInMatrixAsGraph> allCircleIndex = new ArrayList<>();

        for (var i :m_Graph.GetAllVertex()) {

            if(i.equals(i_StartVertex) || i.equals(i_EndVertex)){
                continue;
            }

            m_Graph.SetValue(i,0);
            bfsTree = FindComponent(i_StartVertex);

            if(bfsTree.contains(i_EndVertex) ){
                allCircleIndex.add(i);
            }
            m_Graph.SetValue(i,1);
        }

        return allCircleIndex;
    }

    /**
     * Ex-2 find short route
     * @param i_EndVertex
     * @param i_BfsTreeSpecificVertex
     * @return Collection<VertexInMatrixAsGraph> route
     */
    @NotNull
    private Collection<VertexInMatrixAsGraph> findShortRoute(VertexInMatrixAsGraph i_EndVertex, Collection<VertexInMatrixAsGraph> i_BfsTreeSpecificVertex) {

        VertexInMatrixAsGraph currentVertex = i_EndVertex;

        for (VertexInMatrixAsGraph vertex : i_BfsTreeSpecificVertex) {//Find end_vertex in tree
            if (currentVertex.equals(vertex)) {
                currentVertex = vertex;
                break;
            }
        }

        List<VertexInMatrixAsGraph> routeReverse = new ArrayList<>();

        routeReverse.add(currentVertex);
        while (true) {//Go back until father of father is null

            for (VertexInMatrixAsGraph Vertex : i_BfsTreeSpecificVertex) {//Find father
                if (currentVertex.GetParentName().equals(Vertex.toString())) {
                    currentVertex = Vertex;
                    break;
                }
            }
            routeReverse.add(currentVertex);//Add father to routereverse
            if (currentVertex.GetParentName() == null) {//if father of my father NUll then break
                break;
            }
        }

        Collection<VertexInMatrixAsGraph> route = new ArrayList<>();//Correct list order (Reverse)
        for (int i = routeReverse.size()-1; i >= 0 ; i-- ) {
            route.add(routeReverse.get(i));
        }
        return route;
    }

    /**
     * Ex-3 Find Number Of The Correct Submarines
     * @param i_Matrix
     * @return int all correct components(Submarines)
     */
    @NotNull
    public int Ex3FindNumberOfTheCorrectSubmarines(int[][] i_Matrix){

        m_Graph = new MatrixAsGraph(i_Matrix);

        return FindNumberOfTheCorrectSubmarines();
    }

    /**
     * Ex-3 FindNumberOfTheCorrectSubmarines
     * @return int numberOfTheCorrectSubmarines
     */
    @NotNull
    private int FindNumberOfTheCorrectSubmarines(){

        Collection<HashSet<VertexInMatrixAsGraph>> allComponents = FindAllComponents();//Find all components
        List<HashSet<VertexInMatrixAsGraph>> suspiciousSubmarines = new ArrayList<>();
        int numberOfTheCorrectSubmarines = 0;

        //will check if each component size is bigger than 1 and add it to suspiciousSubmarines (Otherwise not a legal submarine)
        for (HashSet<VertexInMatrixAsGraph> groupOfVertex: allComponents) {
            if(groupOfVertex.size() > 1)
                suspiciousSubmarines.add(groupOfVertex);
        }

        if(suspiciousSubmarines.size() > 0){
            //check if each component(Submarine) estimated size according to Index values is equal to number of Indexes
            for (HashSet<VertexInMatrixAsGraph> submarine :suspiciousSubmarines) {
                if(CheckIfSubmarine(submarine)){
                    numberOfTheCorrectSubmarines++;
                }
            }
        }

        //if components(Submarines) will not be equal to the correct sizes we will return 0 ,otherwise we will return amount of correct submarines
        return numberOfTheCorrectSubmarines;
    }

    /**
     * Ex-3 Will check if component(Submarine) is in a proper size
     * @param i_Submarine
     * @return Boolean isSubmarine (correct component - Submarine)
     */
    @NotNull
    private Boolean CheckIfSubmarine(HashSet<VertexInMatrixAsGraph> i_Submarine){

        boolean isSubmarine = true;

        //Check what is the estimated size of the matrix should be according to the indexes
        int currentSubmarineSize = getSubmarineSize(i_Submarine);

        if(currentSubmarineSize != i_Submarine.size()){
            isSubmarine = false;
        }

        return isSubmarine;
    }

    /**
     * Ex-3 Getting size of the matrix that should be according to the indexes
     * @param i_Submarine
     * @return int SubmarineSize component(submarine) estimated size
     */
    @NotNull
    private int getSubmarineSize(HashSet<VertexInMatrixAsGraph> i_Submarine) {

        int widthMax = 0 ,widthMin = 0;
        int lengthMax = 0,lengthMin = 0 ;
        int SubmarineSize = 0;

        widthMax = widthMin = i_Submarine.iterator().next().GetRow();
        lengthMin = lengthMax = i_Submarine.iterator().next().GetColumn();

        for (VertexInMatrixAsGraph submarineVertex: i_Submarine) {

            //find Column max size
            if(submarineVertex.GetColumn() > lengthMax){
                lengthMax = submarineVertex.GetColumn();
            }
            else if(submarineVertex.GetColumn() < lengthMin){
                lengthMin = submarineVertex.GetColumn();
            }

            //find Row max size
            if(submarineVertex.GetRow() > widthMax){
                widthMax = submarineVertex.GetRow();
            }
            else if(submarineVertex.GetRow() < widthMin){
                widthMin = submarineVertex.GetRow();
            }
        }
        SubmarineSize = (widthMax - widthMin + 1) * (lengthMax - lengthMin + 1);

        return SubmarineSize;
    }
}


