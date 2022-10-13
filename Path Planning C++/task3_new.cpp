#include <bits/stdc++.h>
#include <iostream>
#include <string.h>
#include <stdlib.h>
#include <cmath>
#include <climits>
#include <set>
#include"rapidxml\rapidxml_utils.hpp"  // including the necessary header files

using namespace std;
using namespace rapidxml;

unordered_map<string,int> map_nodes;
// it is the map with key as node id(string) and value as the position of corresponding id in the nodes vector
unordered_map<string,pair<double,double>> map_cordis;
// it is the map with key as node id(string) and value as the pair of latitude and longitude of corresponding node

//this calculates distance between 2 nodes 
//using haversine formula
double dist(string a,string b)
{
    double dist,lat,lon,lat2,lon2;
    lat = map_cordis[a].first;
    lon = map_cordis[a].second;          // latitude and longitude of first node with node_id as string a
    lat2 = map_cordis[b].first;
    lon2 = map_cordis[b].second;         // latitude and longitude of second node with node_id as string b
    dist=0;
                                         // initializing the distance between nodes as 0
    double pi=2*acos(0.0);
    lat=lat*pi/180;
    lon=lon*pi/180;
    lon2=lon2*pi/180;
    lat2=lat2*pi/180;

    double diff1 = lon2 - lon;
    double diff2 = lat2 - lat;
    dist = pow(sin(diff2/2),2)+cos(lat)*cos(lat2)*pow(sin(diff1/2),2);         // using haver-sine formula for distance between two points
    dist = 2*asin(sqrt(dist));
    double earth = 6371;
    dist= dist*earth;
    // finding the distance between the given nodewith each node element and storing it in d
    return dist;
}

//this is dijkstra's algorithm implementation
void algo(vector<pair<double,string>> v[],string node_id_1,string node_id_2,vector<string> nodes)
{
    vector<double> meas(43993,100000); //this stores the shortest path distance from node1
    meas[map_nodes[node_id_1]]=0;
    priority_queue < pair<double,string> , vector< pair<double,string> >,greater< pair<double,string> > > pq; //min-heap.......pair has dist,index
    pq.push(make_pair(0,node_id_1)); //source node is added
    while(!pq.empty())
    {
        string x_=pq.top().second; //this is the pair with the least distance from source node
        int x=map_nodes[x_];
        pq.pop();
        if(x_ == node_id_2) break;
        vector<pair<double,string>>::iterator y;
        for(y=v[x].begin();y!=v[x].end();y++)
        {
            double dist=(*y).first;
            string id=(*y).second;
            if(meas[x]+ dist<meas[map_nodes[id]]) //since distance through x is less it is updated to the lesser value
            {
                meas[map_nodes[id]]=meas[x]+ dist;
                pq.push(make_pair(meas[map_nodes[id]],id));
            }
        }
    }
    if (meas[map_nodes[node_id_2]] == 100000){
        cout<<"No path exists\n";
    }
    else cout<<"The distance is "<<meas[map_nodes[node_id_2]]<<" km !"<<endl;
}

int main()
{
    rapidxml::file<> xmlFile("map.osm");
    rapidxml::xml_document<> doc;
    doc.parse<0>(xmlFile.data());
    //node points to the first occurence of element "node"

    int indice = 0; 
    vector<string> nodes; //this will store ids of nodes as strings
    xml_node<> *node = doc.first_node();

    //iterating through all nodes
    for(xml_node<> *child = node->first_node("node"); child; child = child->next_sibling("node"))
    {
        string s = child->first_attribute("id")->value();
        double lat = stod(child->first_attribute("lat")->value());
        double lon = stod(child->first_attribute("lon")->value());
        map_cordis[s] = {lat,lon};
        nodes.push_back(s);
        map_nodes[s] = indice;
        indice++;
        // cout << child->name() << "\n";
    }
    
    //creating incidence data structure
    vector<pair<double,string>> v[43993];
    //dist,node_id

    //iterating through ways and adding distances in from nodes in v
    for(xml_node<> *child = node->first_node("way"); child; child = child->next_sibling("way"))
    {
        for(xml_node<> *child2 = child->first_node(); child2->next_sibling(); child2 = child2->next_sibling())
        {
             xml_attribute<> *a = child2->first_attribute("ref");
             xml_attribute<> *b = child2->next_sibling()->first_attribute("ref");
             if(!a) continue;
             if(!b) continue;
             string ida = a->value();
             string idb = b->value();
             double disto=dist(a->value(),b->value());
             //cout<<disto<<"\n";
             v[map_nodes[ida]].push_back(make_pair(disto,idb)); //distance from b pushed in vector of a
             v[map_nodes[idb]].push_back(make_pair(disto,ida)); //distance from a pushed in vector of b
        }
    }
    
    //our source
    cout<<"Enter node-1 id\n";
    string node_id_1;
    cin>>node_id_1;                     
    
    //our destination
    cout<<"Enter node-2 id\n";
    string node_id_2;
    cin>>node_id_2;

    algo(v,node_id_1,node_id_2,nodes);   // calculating the minimum distance between 
    // djikstra's algorithm
    return 0;
}