/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.atomique.ksar.Graph;

import java.awt.LayoutManager;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.atomique.ksar.UI.NaturalComparator;
import net.atomique.ksar.UI.ParentNodeInfo;
import net.atomique.ksar.UI.SortedTreeNode;
import net.atomique.ksar.UI.TreeNodeInfo;
import net.atomique.ksar.XML.GraphConfig;
import net.atomique.ksar.kSar;
import org.jfree.data.time.Second;

/**
 *
 * @author alex
 */
public class List {

    public List(kSar hissar,  GraphConfig g,String stitle, String sheader, int i) {
        mysar = hissar;
        HeaderStr = sheader;
        graphconfig =g ;
        Title = stitle;
        skipColumn = i;
        ParentNodeInfo tmp = new ParentNodeInfo(Title, this);
        parentTreeNode = new SortedTreeNode(tmp);
        mysar.add2tree(mysar.graphtree, parentTreeNode);
    }

    public int parse_line(Second now,String s) {
        String cols[] = s.split("\\s+");
        Graph tmp = null;
        if ( ! nodeHashList.containsKey(cols[skipColumn])) {
            tmp= new Graph(mysar, graphconfig,Title + " " + cols[skipColumn], HeaderStr, skipColumn+1, null);
            nodeHashList.put(cols[skipColumn], tmp);
            TreeNodeInfo infotmp= new TreeNodeInfo( cols[skipColumn], tmp);
            SortedTreeNode nodetmp = new SortedTreeNode(infotmp);
            mysar.add2tree(parentTreeNode, nodetmp);
        } else {
            tmp = (Graph)nodeHashList.get(cols[skipColumn]);
        }

        return tmp.parse_line(now,s);
    }


    public JPanel run() {
        JPanel tmppanel = new JPanel();
        LayoutManager tmplayout = null;
        int graphnumber = nodeHashList.size();
        int linenum = (int) Math.floor(graphnumber / 2);
        if (graphnumber % 2 != 0) {
            linenum++;
        }
        tmplayout = new java.awt.GridLayout(linenum, 2);
        tmppanel.setLayout(tmplayout);


        for (Graph graph : nodeHashList.values()) {
            tmppanel.add(graph.get_ChartPanel());
        }

        return tmppanel;
    }

    public boolean isPrintSelected() {
        boolean leaftoprint = false;
        for (Graph graph : nodeHashList.values()) {
            if (graph.printSelected) {
                leaftoprint = true;
                break;
            }
        }
        return leaftoprint;
    }

    public String getTitle() {
        return Title;
    }

    public JPanel getprintform() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(Title));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.PAGE_AXIS));
        return panel;
    }


    protected GraphConfig graphconfig = null;
    protected SortedTreeNode parentTreeNode = null;
    protected kSar mysar = null;
    protected String HeaderStr = null;
    protected SortedMap<String, Graph> nodeHashList = new TreeMap<>(NaturalComparator.NULLS_FIRST);
    protected int skipColumn = 0;
    protected String Title = null;
    
}
