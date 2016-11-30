
import controller.RemoteGames;
import controller.Simulator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Coordinates;
import model.entities.Entity;
import view.DrawController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gillesbraun
 */
public class MainFrame extends javax.swing.JFrame {
    private Simulator sim = new Simulator(10, 15);
    private Preferences prefs;
    private Tool selectedTool = Tool.Add;
    private EntityEnum selectedEntity = Arrays.asList(EntityEnum.values()).get(0);
    private Coordinates pressedCoords = null;
    private Entity currentMovingEntity = null;
    private String resetJson = null;
    
    private HashMap<Integer, String> games = null;
    
    private enum EntityEnum {
        Hitman, Person
    }
    
    private enum Tool {
        Move, Delete, Add
    }
    
    private Timer t = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            sim.doSimulationCycle();
            drawPanel.repaint();
        }
    });
    
    private MouseAdapter drawPanelMouseEvents = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            pressedCoords = DrawController.getCoordinatesForXY(e.getPoint());
            switch(selectedTool) {
                case Add:
                    sim.addEntity(getNewEntityFromSelection(), pressedCoords);
                    break;
                case Delete:
                    sim.getCell(pressedCoords).clear();
                    break;
                case Move:
                    currentMovingEntity = sim.getCell(pressedCoords).getEntity();
                    DrawController.startMovingEntity(currentMovingEntity, e.getPoint());
                    break;
            }
            drawPanel.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Coordinates releaseCoords = DrawController.getCoordinatesForXY(e.getPoint());
            switch(selectedTool) {
                case Move:
                    if(pressedCoords != null && releaseCoords != null && !pressedCoords.equals(releaseCoords)) {
                        sim.changeEntityPosition(pressedCoords, releaseCoords);
                        DrawController.stopMovingEntity();
                    }
                    break;
            }
            pressedCoords = null;
            drawPanel.repaint();
        }
    };
    
    private MouseMotionAdapter drawPanelMouseMotionAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            switch(selectedTool) {
                case Move:
                    if(pressedCoords != null && currentMovingEntity != null) {
                        DrawController.moveMovingEntity(e.getPoint());
                        drawPanel.repaint();
                    }
                    break;
            }
        }
    
    };
    
    private ActionListener radioActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(toolDeleteRadio.isSelected()) {
                selectedTool = Tool.Delete;
            }
            if(toolMoveRadio.isSelected()) {
                selectedTool = Tool.Move;
            }
            if(toolPlaceRadio.isSelected()) {
                selectedTool = Tool.Add;
            }
        }
    };
    
    private ListSelectionListener entiySelectedListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectedEntity = (EntityEnum) entityList.getSelectedValue();
        }
    };
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        drawPanel.setSim(sim);
        setLocationByPlatform(true);
        prefs = Preferences.userNodeForPackage(sim.getClass());
        drawPanel.addMouseListener(drawPanelMouseEvents);
        drawPanel.addMouseMotionListener(drawPanelMouseMotionAdapter);
        toolDeleteRadio.addActionListener(radioActionListener);
        toolMoveRadio.addActionListener(radioActionListener);
        toolPlaceRadio.addActionListener(radioActionListener);
        entityList.addListSelectionListener(entiySelectedListener);
        // Fill Jlist with entities from enum
        entityList.setListData(Arrays.asList(EntityEnum.values()).toArray());
        entityList.setSelectedIndex(0);
    }
    
    private Entity getNewEntityFromSelection() {
        try {
            Class<?> en = Class.forName("model.entities."+selectedEntity.toString());
            List<Constructor<?>> consList = Arrays.asList(en.getConstructors());
            Constructor<?> c = consList.get(0);
            Entity e = (Entity) c.newInstance();
            return e;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void updateSimulator(Simulator s) {
        sim = s;
        drawPanel.setSim(sim);
        drawPanel.repaint();
        resetSimButton.setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolsButtonGroup = new javax.swing.ButtonGroup();
        jMenuItem1 = new javax.swing.JMenuItem();
        drawPanel = new DrawPanel();
        resetSimButton = new javax.swing.JButton();
        startStopSimButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        toolMoveRadio = new javax.swing.JRadioButton();
        toolDeleteRadio = new javax.swing.JRadioButton();
        toolPlaceRadio = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        entityList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newGameMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        loadMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        saveRemoteMenuItem = new javax.swing.JMenuItem();
        loadRemoteMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulator Editor");

        javax.swing.GroupLayout drawPanelLayout = new javax.swing.GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        resetSimButton.setText("Reset");
        resetSimButton.setEnabled(false);
        resetSimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetSimButtonActionPerformed(evt);
            }
        });

        startStopSimButton.setText("Start");
        startStopSimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopSimButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Time controls");

        jLabel2.setText("Tools");

        toolsButtonGroup.add(toolMoveRadio);
        toolMoveRadio.setText("Move");

        toolsButtonGroup.add(toolDeleteRadio);
        toolDeleteRadio.setText("Delete");

        toolsButtonGroup.add(toolPlaceRadio);
        toolPlaceRadio.setSelected(true);
        toolPlaceRadio.setText("Add Entity");

        entityList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(entityList);

        jLabel3.setText("Entities");

        fileMenu.setText("File");

        newGameMenuItem.setText("New Game ...");
        newGameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newGameMenuItem);
        fileMenu.add(jSeparator3);

        saveMenuItem.setText("Save ...");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        loadMenuItem.setText("Load ...");
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadMenuItem);
        fileMenu.add(jSeparator2);

        saveRemoteMenuItem.setText("Save Remote");
        saveRemoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveRemoteMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveRemoteMenuItem);

        loadRemoteMenuItem.setText("Load Remote ...");
        loadRemoteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadRemoteMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadRemoteMenuItem);
        fileMenu.add(jSeparator1);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(startStopSimButton)
                    .addComponent(jLabel3)
                    .addComponent(resetSimButton)
                    .addComponent(jLabel2)
                    .addComponent(toolDeleteRadio)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toolMoveRadio)
                    .addComponent(toolPlaceRadio))
                .addGap(18, 18, 18)
                .addComponent(drawPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startStopSimButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetSimButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolPlaceRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolDeleteRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolMoveRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(drawPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startStopSimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStopSimButtonActionPerformed
        if(t.isRunning()) {
            t.stop();
            startStopSimButton.setText("Start");
        } else {        
            resetJson = sim.getJson();
            resetSimButton.setEnabled(true);
            t.start();
            startStopSimButton.setText("Pause");
        }
    }//GEN-LAST:event_startStopSimButtonActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        String path = prefs.get("LAST_SAVE_DIR" , System.getProperty("user.home"));
        JFileChooser fileChooser = new JFileChooser(new File(path));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Json Files (*.json)", "json"));
        fileChooser.setMultiSelectionEnabled(false);
        int ret = fileChooser.showDialog(this, "Save File");
        if(ret == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String filepath = file.getAbsolutePath();
                if( ! filepath.endsWith(".json")) {
                    filepath += ".json";
                }
                sim.saveToFile(filepath);
                prefs.put("LAST_SAVE_DIR", file.getPath());
            } catch (IOException ex) {
                showErrorDialog(ex.getLocalizedMessage(), "Error saving file");
            }
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuItemActionPerformed
        String path = prefs.get("LAST_SAVE_DIR" , System.getProperty("user.home"));
        JFileChooser fileChooser = new JFileChooser(new File(path));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Json Files (*.json)", "json"));
        fileChooser.setMultiSelectionEnabled(false);
        int ret = fileChooser.showDialog(this, "Load File");
        if(ret == JFileChooser.APPROVE_OPTION) {
            try {
                sim.loadFromFile(fileChooser.getSelectedFile().getAbsolutePath());
                drawPanel.repaint();
            } catch (IOException ex) {
                showErrorDialog(ex.getMessage(), "Error loading file");
            }
        }
    }//GEN-LAST:event_loadMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void saveRemoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveRemoteMenuItemActionPerformed
        
        try {
            RemoteGames.saveGametoRemote(sim);
            JOptionPane.showMessageDialog(this, "Saving successful", "Simulator", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage(), "Error saving");
        }
    }//GEN-LAST:event_saveRemoteMenuItemActionPerformed

    private void loadRemoteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadRemoteMenuItemActionPerformed
        try {
            games = RemoteGames.getGames();
            LoadGameDialog dialog = new LoadGameDialog(this, true, games);
            dialog.setVisible(true);
            String selectedJson = dialog.getSelectedJson();
            if(selectedJson != null) {
                updateSimulator(Simulator.fromJson(selectedJson));
                
            }
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage(), "Error loading");
        }
    }//GEN-LAST:event_loadRemoteMenuItemActionPerformed

    private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameMenuItemActionPerformed
        NewGameDialog newGameDialog = new NewGameDialog(this, true);
        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
        Integer rows = newGameDialog.getRows();
        Integer cols = newGameDialog.getCols();
        if(rows != null && cols != null) {
            t.stop();
            updateSimulator(new Simulator(rows, cols));
        }
    }//GEN-LAST:event_newGameMenuItemActionPerformed

    private void resetSimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetSimButtonActionPerformed
        t.stop();
        updateSimulator(Simulator.fromJson(resetJson));
        resetSimButton.setEnabled(false);
    }//GEN-LAST:event_resetSimButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private DrawPanel drawPanel;
    private javax.swing.JList entityList;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem loadMenuItem;
    private javax.swing.JMenuItem loadRemoteMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newGameMenuItem;
    private javax.swing.JButton resetSimButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem saveRemoteMenuItem;
    private javax.swing.JButton startStopSimButton;
    private javax.swing.JRadioButton toolDeleteRadio;
    private javax.swing.JRadioButton toolMoveRadio;
    private javax.swing.JRadioButton toolPlaceRadio;
    private javax.swing.ButtonGroup toolsButtonGroup;
    // End of variables declaration//GEN-END:variables
}
