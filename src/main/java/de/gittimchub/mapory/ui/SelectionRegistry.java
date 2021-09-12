package de.gittimchub.mapory.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SelectionRegistry {

    private List<File> selected;

    private static SelectionRegistry instance;
    private FolderViewer viewer;

    private SelectionRegistry(){
        this.selected = new ArrayList<>();
    }

    public static SelectionRegistry getInstance() {
        if(instance==null){
            instance = new SelectionRegistry();
        }
        return instance;
    }


    public void setViewer(FolderViewer viewer){
        this.viewer = viewer;
    }
    private void updateViewer(){
        if(this.viewer != null){
            this.viewer.updateSelectedListData();
        }
    }

    public void add(File f){
        if(!this.selected.contains(f)){
            this.selected.add(f);
            this.updateViewer();
        }
    }

    public void addAllFromFolder(File f){
        if(f.isDirectory()){
            this.selected = Arrays.stream(Objects.requireNonNull(f.listFiles())).filter(File::isFile).collect(Collectors.toList());
            this.updateViewer();
        }
    }

    public List<String> getFilenames(){
        return this.selected.stream().map(File::getName).collect(Collectors.toList());
    }

    public List<File> getFiles(){
        return this.selected;
    }

    public void removeAll(){
        this.selected = null;
        this.selected = new ArrayList<>();
        this.updateViewer();
    }

}
