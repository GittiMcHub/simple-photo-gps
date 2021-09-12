package de.gittimchub.mapory.ui;

import de.gittimchub.mapory.ui.components.FolderTree;
import de.gittimchub.mapory.ui.components.GeocodePanel;
import de.gittimchub.mapory.ui.components.ThumbnailPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FolderViewer extends JFrame {

    private static final Dimension WINDOW_SIZE = new Dimension(1820, 980);

    private final String path;

    JPanel mainPanel;
    private FolderTree folderTree;

    private JScrollPane thumbnailScroller;
    private ThumbnailPanel thumbnailsPanel;
    private JList<String> selectedList;
    private JPanel selectionListPanel;
    private JButton btAddAllFromFolder;


    public FolderViewer(String path) {
        this.path = path;

        this.setPreferredSize(WINDOW_SIZE);
        this.setSize(WINDOW_SIZE);

        SelectionRegistry.getInstance().setViewer(this);
        this.setContentPane(initContentPane());
        this.configureFolderTreeOnClick();


        this.pack();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private Container initContentPane() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());

        this.mainPanel.add( new JScrollPane(getTreeComponent()), BorderLayout.LINE_START);
        this.mainPanel.add(getThumbnailScroller(), BorderLayout.CENTER);
        this.mainPanel.add(getSelectionListPanel(), BorderLayout.LINE_END);

        this.mainPanel.add(new GeocodePanel(), BorderLayout.PAGE_END);

        return this.mainPanel;
    }

    private FolderTree getTreeComponent() {
        if(this.folderTree == null){
            this.folderTree = new FolderTree(this.path);

        }
        return folderTree;
    }

    private JScrollPane getThumbnailScroller(){
        if(this.thumbnailScroller == null){
            thumbnailScroller = new JScrollPane(this.thumbnailsPanel);
        }
        return this.thumbnailScroller;
    }


    private File getSelectedFolder(){
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) folderTree.getLastSelectedPathComponent();
        return (File) root.getUserObject();
    }

    private JPanel getThumbnails() throws IOException {
        if(this.thumbnailsPanel == null){
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.folderTree.getModel().getRoot();
            File userObj = (File) root.getUserObject();
            this.thumbnailsPanel = new ThumbnailPanel(userObj.getPath());
        }
        return this.thumbnailsPanel;
    }

    private void replaceThumbnails(String path) throws IOException {
        this.remove(this.getThumbnailScroller());
        this.remove(this.getThumbnails());

        this.thumbnailsPanel =  new ThumbnailPanel(path);
        this.thumbnailScroller = new JScrollPane(this.thumbnailsPanel);

        this.add( this.thumbnailScroller, BorderLayout.CENTER);
        this.revalidate();
    }

    private JPanel getSelectionListPanel(){
        if(this.selectionListPanel == null){
            this.selectionListPanel = new JPanel();
            this.selectionListPanel.setLayout(new BorderLayout());
            this.selectionListPanel.add(this.getSelectedList(), BorderLayout.CENTER);
            JButton removeAllButton = new JButton("Remove all");
            removeAllButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SelectionRegistry.getInstance().removeAll();
                }
            });
            JButton btAddFolder = new JButton("Add folder");
            btAddFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SelectionRegistry.getInstance().addAllFromFolder(getSelectedFolder());
                }
            });
            this.selectionListPanel.add(btAddFolder, BorderLayout.PAGE_START);
            this.selectionListPanel.add(removeAllButton, BorderLayout.PAGE_END);
        }
        return this.selectionListPanel;
    }

    private JList<String> getSelectedList(){
        if(this.selectedList == null){
            this.selectedList = new JList<>();
            this.updateSelectedListData();
        }
        return this.selectedList;
    }

    public void updateSelectedListData(){
        String[] array = new String[SelectionRegistry.getInstance().getFiles().size()];
        SelectionRegistry.getInstance().getFilenames().toArray(array);
        if(array.length == 0){
            this.selectedList.setListData(new String[]{" -- EMPTY --- "});
        } else {
            this.selectedList.setListData(array);
        }

        this.selectedList.revalidate();
    }



    private void configureFolderTreeOnClick(){
        this.getTreeComponent().addTreeSelectionListener(e -> {
            JFrame waitFrame = getWaitFrame();
            try {
                getTreeComponent().addSubdirsOnClick(getSelectedFolder(), (DefaultMutableTreeNode) e.getPath().getLastPathComponent());
                replaceThumbnails(getSelectedFolder().getPath());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            waitFrame.dispose();
        });
    }

    private JFrame getWaitFrame() {
        JFrame waitFrame = new JFrame();
        waitFrame.setTitle("Please wait....");
        waitFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        waitFrame.setPreferredSize(new Dimension(400,75));
        waitFrame.add(new JLabel("......................"));
        waitFrame.pack();
        waitFrame.setAlwaysOnTop(true);
        waitFrame.setLocationRelativeTo(mainPanel);
        waitFrame.setVisible(true);
        return waitFrame;
    }

}
