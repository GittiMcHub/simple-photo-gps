package de.gittimchub.mapory.ui.components;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

public class FolderTree extends JTree {

    DefaultMutableTreeNode root;
    DefaultTreeModel model;

    public FolderTree(String path){
        super();
        File fileRoot = new File(path);
        this.root = new DefaultMutableTreeNode(fileRoot);
        this.model = new DefaultTreeModel(root);

        File[] subItems = fileRoot.listFiles();
        for(File f : subItems){
            if(f.isDirectory()){
                this.root.add(new DefaultMutableTreeNode(f));
            }
        }
        this.setModel(model);
    }

    public void addSubdirsOnClick(File dir, DefaultMutableTreeNode node){
        if(dir.isFile()){
            System.out.println("Cannot to Tree as Object is a file, only directorys allowed!");
            return;
        }
        for(File f : dir.listFiles()){
            if(f.isDirectory()){
               node.add(new DefaultMutableTreeNode(f));
            }
        }
    }



}
