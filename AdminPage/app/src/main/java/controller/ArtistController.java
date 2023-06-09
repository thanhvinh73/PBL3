package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import adminpage.App;
import controller.add.AlbumAddController;
import controller.add.ArtistAddController;
import controller.add.SongAddController;
import controller.edit.ArtistEditController;
import httprequest.IArtistResponse;
import httprequest.IMyFileResponse;
import httprequest.implement.ArtistResponseImpl;
import httprequest.implement.MyFileResponseImpl;
import lombok.Getter;
import lombok.Setter;
import models.ArtistModel;
import view.ArtistView;
import view.ToastMessage;

public class ArtistController implements ActionListener, WindowListener, DocumentListener {
    private String searchStr_old = "";
    private int isSearchStrEmpty = 0;
    private final ArtistView artistView;

    @Getter
    private static final ArtistAddController artistAddController = new ArtistAddController();
    @Getter
    private static final ArtistEditController artistEditController = new ArtistEditController();

    @Setter
    private SongController songController;
    @Setter
    private AlbumController albumController;
    @Setter
    private SongAddController songAddController;
    @Setter
    private AlbumAddController albumAddController;

    @Setter
    private IArtistResponse iArtistResponse;
    @Setter
    private IMyFileResponse iMyFileResponse;

    public ArtistController() {
        artistView = ArtistView.getInstance();
        artistView.addActionListener(this);
        artistView.addWindowListener(this);
        artistView.addDocumentListener(this);

        iArtistResponse = new ArtistResponseImpl();
    }

    private void showAlbum() {
        int row = artistView.artistTable.getSelectedRow();
        if (row != -1) {
            try {
                ArtistModel artistModel = iArtistResponse
                        .findById(Integer.parseInt(artistView.artistTable.getValueAt(row, 0).toString().trim()));

                setAlbumController(HomePageController.getAlbumController());

                artistView.setEnabled(false);
                albumController.setArtistId(artistModel.getArtistId());
                albumController.artistAlbumView(artistModel.getArtistName(), 1102, 519);
                albumController.showGUI(artistModel.getAlbums());
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(artistView, e1.getMessage(), "Invalid format artist's ID",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(artistView, e1.getMessage(), "Not found object",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSingleAndEpSong() {
        int row = artistView.artistTable.getSelectedRow();
        if (row != -1) {
            try {
                ArtistModel artistModel = iArtistResponse
                        .findById(Integer.parseInt(artistView.artistTable.getValueAt(row, 0).toString().trim()));

                setSongController(HomePageController.getSongController());

                artistView.setEnabled(false);
                songController.changeSongView(artistModel.getArtistName(), 1102, 519, true);
                songController.showGUI(artistModel.getSingleAndEpSongs());
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(artistView, e1.getMessage(), "Invalid format artist's ID",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(artistView, e1.getMessage(), "Not found object",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editArtist() {
        int row = artistView.artistTable.getSelectedRow();
        if (row != -1) {
            try {
                ArtistModel artistModel = iArtistResponse
                        .findById((Integer.parseInt(artistView.artistTable.getValueAt(row, 0).toString().trim())));

                artistView.setEnabled(false);
                artistEditController.showGUI(artistModel);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(artistView, e.getMessage(), "Invalid format artist's ID",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(artistView, e.getMessage(), "Not found object",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteArtist() {
        int row = artistView.artistTable.getSelectedRow();
        if (row != -1) {
            try {
                int option = JOptionPane.showConfirmDialog(artistView, "Are you sure?", "DELETE OBJECT",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option == 0) {
                    ArtistModel artistModel = iArtistResponse
                            .findById(Integer.parseInt(artistView.artistTable.getValueAt(row, 0).toString().trim()));

                    try {
                        setIArtistResponse(new ArtistResponseImpl());
                        setIMyFileResponse(new MyFileResponseImpl());

                        iArtistResponse.deleteById(artistModel.getArtistId());
                        artistView.setArtistTable(iArtistResponse.findAll());
                        ToastMessage toastMessage = new ToastMessage(
                                String.format("Delete artist '%s' successfully!", artistModel.getArtistName()));
                        toastMessage.showToast(
                                artistView.getX() + (artistView.getWidth() / 2) - (toastMessage.getWidth() / 4),
                                artistView.getY() + artistView.getHeight() - 55);

                    } catch (NumberFormatException e1) {
                        throw new Exception("Invalid format artist image file's ID");
                    }
                }

            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(artistView, e1.getMessage(), "Invalid format artist's ID",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(artistView, e1.getMessage(), "Not found object",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showUpdateToastMessage(int artistId) {
        ToastMessage toastMessage = new ToastMessage(
                String.format("Update artist with '%s' successfully!",
                        String.valueOf(artistId)));
        toastMessage.showToast(
                artistView.getX() + (artistView.getWidth() / 2) - (toastMessage.getWidth() / 4),
                artistView.getY() + artistView.getHeight() - 55);
    }

    public void showCreateToastMessage() {
        ToastMessage toastMessage = new ToastMessage("Create new artist successfully!");
        toastMessage.showToast(
                artistView.getX() + (artistView.getWidth() / 2) - (toastMessage.getWidth() / 4),
                artistView.getY() + artistView.getHeight() - 55);
    }

    public void setEnabled(boolean enabled) {
        artistView.setEnabled(enabled);
    }

    public void setArtistTable(List<ArtistModel> lArtistModels) {
        artistView.setArtistTable(lArtistModels);
    }

    public void showGUI() {
        artistView.setVisible(true);
    }

    public void showGUI(List<ArtistModel> listArtistModels) {
        artistView.setArtistTable(listArtistModels);
        artistView.setVisible(true);
    }

    public void chooseSongArtistMode() {
        artistView.chooseSongArtistMode();
    }

    public void chooseAlbumArtistMode() {
        artistView.chooseAlbumArtistMode();
    }

    public int getArtistId() {
        int row = artistView.artistTable.getSelectedRow();
        if (row != -1) {
            try {
                int result = Integer.parseInt(artistView.artistTable.getValueAt(row, 0).toString().trim());
                return result;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(artistView, ex.getMessage(), "Invalid format artist's ID",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == artistView.btnShowAlbum) {
            showAlbum();
        } else if (e.getSource() == artistView.btnShowSong) {
            showSingleAndEpSong();
        } else if (e.getSource() == artistView.btnAdd) {
            artistView.setEnabled(false);
            artistAddController.showGUI();
        } else if (e.getSource() == artistView.btnEdit) {
            editArtist();
        } else if (e.getSource() == artistView.btnDelete) {
            deleteArtist();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        try {
            artistView.setArtistTable(iArtistResponse.findAll());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (artistView.isChooseSongArtistMode()) {
            artistView.reset();
            setSongAddController(SongController.getSongAddController());
            songAddController.setArtistId(getArtistId());
            songAddController.showGUI();
        } else if (artistView.isChooseAlbumArtistMode()) {
            artistView.reset();
            setAlbumAddController(AlbumController.getAlbumAddController());
            albumAddController.setArtistId(getArtistId());
            albumAddController.showGUI();
        } else {
            App.homePageController.showGUI();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
                setIArtistResponse(new ArtistResponseImpl());
                String searchStr_new = artistView.txtSearch.getText();
                if (searchStr_new.compareTo(searchStr_old) != 0) {
                    artistView.setArtistTable(iArtistResponse.search(artistView.txtSearch.getText()));
                    searchStr_old = artistView.txtSearch.getText();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
                setIArtistResponse(new ArtistResponseImpl());
                String searchStr_new = artistView.txtSearch.getText();
                if (searchStr_new.compareTo(searchStr_old) != 0) {
                    artistView.setArtistTable(iArtistResponse.search(artistView.txtSearch.getText()));
                    searchStr_old = artistView.txtSearch.getText();
                    isSearchStrEmpty = 0;
                } else if (searchStr_new.isEmpty() && searchStr_old.isEmpty()) {
                    isSearchStrEmpty++;
                }
                if (isSearchStrEmpty == 1) {
                    artistView.setArtistTable(iArtistResponse.search(artistView.txtSearch.getText()));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

}
