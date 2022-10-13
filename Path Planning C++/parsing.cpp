#include<iostream>
#include<algorithm>
#include<vector>
#include<string>
#include<bits/stdc++.h>
#include "rapidxml.hpp"
#include "rapidxml_print.hpp"
#include "rapidxml_utils.hpp"
#include<fstream>
#include<cmath>

#define ll long long 

using namespace rapidxml;
using namespace std;

typedef struct {
    string name;
    int id;
    double lon,lat;
} node;

typedef struct {
    string name;
    int id;
    vector<ll> path; //we will only store the ids (or the refs) of the nodes in a path
    
} way;


// this function checks if input is substring of a
//this function is case insensitive
int ContainsSubString(string nodename, string input){
    int n = input.length();
    string a = nodename;
    if (n > a.length()) return 0;
    for(int i=0;i<a.length();i++){
        a[i] = tolower(a[i]); 
    }
    for(int i=0;i<n;i++){
        input[i] = tolower(input[i]);
    }

    for(int i=0;i<=a.length()-n;i++){
        if (input == a.substr(i,n)){
            return 1;
        }
    }

    return 0;
}

//this function returns the distance between 2 nodes
//using haversine formula
static double haversine(double lat1, double lon1,double lat2, double lon2)
{
    // distance between latitudes
    // and longitudes
    double dLat = (lat2 - lat1) * M_PI / 180.0;
    double dLon = (lon2 - lon1) * M_PI / 180.0;
 
    // convert to radians
    lat1 = (lat1) * M_PI / 180.0;
    lat2 = (lat2) * M_PI / 180.0;
 
    // apply formulae
    double a = pow(sin(dLat / 2), 2) + pow(sin(dLon / 2), 2) * cos(lat1) * cos(lat2);
    double rad = 6371;
    double c = 2 * asin(sqrt(a));
    return rad * c;
}
 

int main(){
 
    //parsing from the map.osm file
    xml_document<> doc;
    ifstream file("map.osm");
    stringstream buffer;
    buffer << file.rdbuf();
    file.close();
    string content(buffer.str());
    doc.parse<0>(&content[0]);

    unordered_map<ll,node> map_node; //map of nodes with keys as id and node data type as value
    unordered_map<ll,way> map_way;  //map of ways with keys as id and way data type as value

    xml_node<> *firstnode = doc.first_node(); //this is a pointer to the first node
    xml_node<> *node_pt = firstnode->first_node("node"); //pointer to the node which will traverse the osm file
    for(;node_pt;node_pt = node_pt->next_sibling("node")){
        xml_attribute<> *idpt = node_pt->first_attribute("id"); 
        xml_attribute<> *latpt = node_pt->first_attribute("lat"); 
        xml_attribute<> *lonpt = node_pt->first_attribute("lon"); 
        ll temp = stoll(idpt->value()); 
        map_node[temp].id = temp; 
        map_node[temp].lon = stod(lonpt->value());
        map_node[temp].lat = stod(latpt->value());

        //basic deatils of nodes (id, latitude and longitude) are stored
        //now we iterate through the tag attributes to find name of the node, if any
        for(xml_node<> *tagptr = node_pt->first_node();tagptr;tagptr = tagptr->next_sibling()){
            xml_attribute<> *attribute_tag = tagptr->first_attribute("k");

            if (strcmp(attribute_tag->value(),"name") == 0 ){
                attribute_tag = tagptr->first_attribute("v");
                
                map_node[temp].name = attribute_tag->value();
            }
        } 
    }
 
    //now we iterate through the ways
    for(node_pt = firstnode->first_node("way");node_pt;node_pt = node_pt->next_sibling("way")){
        xml_attribute<> *idpt = node_pt->first_attribute("id"); 
        ll temp = stoll(idpt->value());
        map_way[temp].id = temp;

        //the id of the way is stored
        //now we iterate through the tag attributes to find name of the way, if any
        //we also store the ids of the nodes on that way
        for(xml_node<> *tagptr = node_pt->first_node();tagptr;tagptr = tagptr->next_sibling()){
            if (strcmp(tagptr->name(),"nd") == 0) {
                xml_attribute<> *attribute_ptr = tagptr->first_attribute("ref");
                ll nid = stoll(attribute_ptr->value());
                map_way[temp].path.push_back(nid);
            }

            if (strcmp(tagptr->name(),"tag") == 0){
                xml_attribute<> *attribute_tag = tagptr->first_attribute("k");
                if (strcmp(attribute_tag->value(),"name") == 0){
                    attribute_tag = tagptr->first_attribute("v");
                    map_way[temp].name = attribute_tag->value();
                }
            }
        } 
    }

    //Use case 1 - task 1: Counting and Printing the number of nodes and ways and allowing user to search
    
    //Task 1(a): Counting the number of nodes
    int total_nodes = 0;
    for(int i=0;i<map_node.bucket_count();i++){
        total_nodes += map_node.bucket_size(i);
    }

    //Task 1(b): Counting the number of ways
    int total_ways = 0;
    for(int i=0;i<map_way.bucket_count();i++){
        total_ways += map_way.bucket_size(i);
    }


    cout<<"\nThe OSM file has been parsed.\n";
    int input = -1;
    while(input != 0){
        cout<<"\nThe following functionalities are available:\n";
        cout<<"1. Display the total number of nodes and ways\n";
        cout<<"2. Search for a substring\n";
        cout<<"3. Find k closest nodes to a given node using crow-fly distance\n";
        cout<<"4. Find the path distance between two given nodes\n";
        cout<<"0. Exit\n";
        cout<<"Enter a code: "; 
        cin>>input;
        switch(input)
        {
            case 1:{
                cout<<"Total nodes in the given map: "<<total_nodes<<"\n";
                cout<<"Total ways in the given map: "<<total_ways<<"\n";
                break;
            } 

            case 2:{
                //Task 1(c): Searching for a substring
                string input;
                cout<<"Enter the name to be searched: ";
                cin>>input;
                int count = 0;

                cout<<"\nSearch results for "<<input<<":\n";
                for(int i=0;i<map_node.bucket_count();i++){
                    for(auto it = map_node.begin(i);it!=map_node.end(i);it++){
                        if (it->second.name.length() == 0) continue; //if no name assigned then continue
                        if ( ContainsSubString(it->second.name,input) == 1){
                            count++;
                            cout<<count<<". Node ID: "<<it->first<<"\tNode Name: "<<it->second.name<<"\tLatitude: "<<it->second.lat<<"\tLongitude: "<<it->second.lon<<"\n";
                        }
                    }       
                }

                for(int i=0;i<map_way.bucket_count();i++){
                    for(auto it = map_way.begin(i);it!=map_way.end(i);it++){
                        if (it->second.name.length() == 0) continue;
                        if ( ContainsSubString(it->second.name,input) == 1){
                        count++;
                        cout<<count<<". Way ID: "<<it->first<<"\tWay Name: "<<it->second.name<<"\n";
                        }
                    }
                }
    
                if (count == 0){
                    cout<<"No search results found\n";
                }
                break;
            }

            case 3:{
                //Use Case-2: find k closest nodes to a given node

                //we implement a min priority queue of type pair<double,ll>
                //double will be the distance and ll will be the id of the node
                priority_queue<pair<double,ll>,vector<pair<double,ll>>,greater<pair<double,ll>>> pq;
                ll input_id;
                int k;  
                cout<<"Enter the node id: ";
                cin>>input_id;
                cout<<"Enter the value of k: ";
                cin>>k;
                double inlat = map_node[input_id].lat;
                double inlon = map_node[input_id].lon;

                if (inlat == 0 && inlon == 0){ // wrong id entered
                    cout<<"No such node exists\n";
                    break;
                }

                //distance from each node is calculated
                //and the pair is pushed into the priority queue
                for(int i=0;i<map_node.bucket_count();i++){
                    for(auto it = map_node.begin(i);it!=map_node.end(i);it++){
                        if (it->second.id == input_id) continue;
                        else{
                            double dist = haversine(inlat,inlon,it->second.lat,it->second.lon);
                            pq.push(make_pair(dist,it->first));
                        }
                    }
                }

                //first k elements are popped and the details are displayed
                cout<<k<<" nearest neighbours of the given node "<<input_id<<":\n";
                for(int i=1;i<=k;i++){
                    pair<double,ll> s = pq.top();
                    cout<<i<<". Node: "<<s.second<<"\tDistance: "<<s.first<<"km\n";
                    pq.pop();
                }
                pq.empty();
                break;
            }

            case 4:{        
                //Use case-3: find shortest path distance between 2 given nodes
                unordered_map<ll,int> map_index; //this map will have key: node id and value: index in adjacency list;
    
                list<pair<double,ll>> adjList[total_nodes]; //array of lists is created
                //the first element will be node id and the second element in pair will be distance 
                //from the head node
                int index = 0;
                for(int i=0;i<map_node.bucket_count();i++){
                for(auto it = map_node.begin(i);it != map_node.end(i);it++){
                    map_index[it->first] = index;
                    index++;
                    }
                }

                //we iterate through all the ways and the nodes and fill the list
                for(int i=0;i<map_way.bucket_count();i++){
                    for (auto it = map_way.begin(i);it!= map_way.end(i);it++){
                        for(int j=1;j<it->second.path.size();j++){
                            ll node1 = it->second.path[j];
                            ll node2 = it->second.path[j-1];
                            double distance = haversine(map_node[node1].lat,map_node[node1].lon,map_node[node2].lat,map_node[node2].lon);
                            adjList[map_index[node1]].push_back(make_pair(distance,node2));
                            adjList[map_index[node2]].push_back(make_pair(distance,node1));
                        }
                    }
                }
                //taking input
                cout<<"Enter id of node 1: ";
                ll id1,id2;
                cin>>id1;
                cout<<"Enter id of node 2: ";
                cin>>id2;

                if ((map_node[id1].lat == 0 && map_node[id1].lon == 0) || (map_node[id2].lon == 0 && map_node[id2].lat == 0)){
                    cout<<"No such node exists\n";
                    break;
                }

                int index1 = map_index[id1]; //index of id1 in adjList
                int index2 = map_index[id2]; //index of id2 in adjList
                set<pair<double,ll>> set_processed; //this will store the nodes that are processed
                vector<double> dist(total_nodes,10000); //this will store of all nodes from id1
                // 10000 is just an arbitrarily large number

                //running dijkstra for id1
                set_processed.insert(make_pair(0,id1)); //source node is added to the set
                dist[index1] = 0;
                while(!set_processed.empty()){
                    pair<double,ll> temp = *(set_processed.begin()); //this will be the node closest to source
                    set_processed.erase(set_processed.begin());
                    int temp2 = map_index[temp.second];
                    list<pair<double,ll>>::iterator it;
                    for( it = adjList[temp2].begin();it != adjList[temp2].end();it++){ //iterating through the neighbours of the closest node
                        int temp3 = map_index[(*it).second];
                        double weight = (*it).first;
                        if (dist[temp3] >  dist[temp2] + weight){
                            if (dist[temp3] != 10000) { //if no direct path exists from the source, dist is 10000
                                set_processed.erase(set_processed.find(make_pair(dist[temp3],(*it).second)));
                            }
                            dist[temp3] = dist[temp2] + weight;
                            set_processed.insert(make_pair(dist[temp3],(*it).second));
                        }        
                    }
                    if (dist[index2] != 10000) break; //this means the closest path between the given 2 nodes has been found and we can exit
                }

                if (dist[index2] == 10000){
                    cout<<"No path found between the given nodes\n";
                }
                else {cout<<"Distance between nodes "<<id1<<" and " <<id2<<" is "<<dist[index2]<<" km.\n";}
                break;
            }   

            case 0:{   
                cout<<"The program has ended\n";
                exit(0);
            }

            default:{
                cout<<"Wrong code entered\n";

            }                
        }

    }
    return 0;
}

