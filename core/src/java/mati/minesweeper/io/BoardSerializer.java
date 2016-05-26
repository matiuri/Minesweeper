package mati.minesweeper.io;

import mati.advancedgdx.io.Serializable;
import mati.minesweeper.board.Board;
import mati.minesweeper.board.Cell;

public class BoardSerializer implements Serializable<Board> {
    private CellSkel[][] cskel;
    private int wh, mines, totalClean, opened, flags;
    private boolean first;

    @Override
    public void preserialize(Board board) {
        cskel = new CellSkel[board.getWh()][board.getWh()];
        for (int x = 0; x < cskel.length; x++) {
            for (int y = 0; y < cskel[x].length; y++) {
                CellSkel skel = new CellSkel();
                skel.set(board.getCells()[x][y]);
                cskel[x][y] = skel;
            }
        }
        wh = board.getWh();
        mines = board.getMines();
        totalClean = board.getTotalClean();
        opened = board.getOpened();
        flags = board.getFCounter().getFlags();
        first = board.getFirst();
    }

    @Override
    public Board recover() {
        Board board = new Board();
        Cell[][] cells = new Cell[wh][wh];
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = cskel[x][y].get(board);
                board.addActor(cells[x][y]);
                cells[x][y].addListener();
            }
        }
        board.setCells(cells);
        board.setMines(mines);
        board.setTotalClean(totalClean);
        board.setOpened(opened);
        board.setFlags(flags);
        board.setFirst(first);
        return board;
    }

    private static class CellSkel {
        private int x, y, aroundMines, aroundFlags;
        private boolean mined, opened, flagged, selected;

        private void set(Cell cell) {
            x = cell.getX();
            y = cell.getY();
            aroundMines = cell.getAroundMines();
            aroundFlags = cell.getAroundFlags();
            mined = cell.getMined();
            opened = cell.getOpened();
            flagged = cell.getFlagged();
            selected = cell.getSelected();
        }

        private Cell get(Board board) {
            Cell cell = new Cell(x, y, board.getSize(), mined, board);
            cell.setAroundMines(aroundMines);
            cell.setAroundFlags(aroundFlags);
            cell.setOpened(opened);
            cell.setFlagged(flagged);
            cell.setSelected(selected);
            return cell;
        }
    }
}
