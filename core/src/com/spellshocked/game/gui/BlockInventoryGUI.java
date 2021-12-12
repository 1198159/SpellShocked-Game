package com.spellshocked.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.spellshocked.game.Spellshocked;
import com.spellshocked.game.entity.PlayerEntity;

import com.spellshocked.game.world.obstacle.Chest;
import com.spellshocked.game.item.Item;
import com.spellshocked.game.item.inventory.Inventory;
import com.spellshocked.game.world.obstacle.ObstacleContainer;
import com.spellshocked.game.world.Tile;
import com.spellshocked.game.world.obstacle.ObstacleEntity;

public class BlockInventoryGUI extends ClickGUI{

    private Item currentItem;
    private Item test1;
    protected Inventory inv;


    public BlockInventoryGUI(Spellshocked g1, PlayerEntity p1) {
        super(g1, p1);
        test1 = new Item("./json/Inventory/Item/Weapon/bucket.json");

    }
    public BlockInventoryGUI setInventory(Inventory i){
        inv = i;
        for (int j = 0; j < inv.size(); j++) {
            if (Math.random() > 0.9){
                inv.set(j, test1);
                return this;
            }
        }
        return this;
    }

    @Override
    public void draw() {
        b = g.world.spriteBatch;
        OrthographicCamera cam = g.world.getC();
//        b.setProjectionMatrix(g.world.getC().combined);
        Vector3 actualMouse = g.world.getMouse();
        b.begin();
        inv.draw(b, cam.position.x-80, cam.position.y-cam.zoom*70);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int cam_x = (int) cam.position.x-80;
            int cam_y = (int) (cam.position.y-cam.zoom*70);
            int i = getFromSlot(actualMouse, cam_x, cam_y);
            if (currentItem != null && i != -1 && inv.get(i) == null) {
                inv.add(i, currentItem);
                currentItem = null;
            }
            else if (currentItem == null && i != -1 && inv.get(i) != null) {
                currentItem = inv.remove(i);
                p.hotbar.add(currentItem);
            }
        }
        if (currentItem != null) {
            b.draw(currentItem, (int) actualMouse.x, (int) actualMouse.y, 24, 24);
        }
        if (currentTile != null && !p.obstacleNear().contains(currentTile)) {
            g.world.activeStages.put(((ObstacleContainer) currentTile.obstacle).getGui(), false);
            changeDisplay();
        }

        // adding and removing items to inv for testing
//        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//            inv.add(test1);
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
//            inv.remove(test1);
//        }
        b.end();

    }

    public int getFromSlot(Vector3 mouse, int camX, int camY) {
        for (int i = 0; i < inv.size(); i++) {
            int intX = (int) mouse.x;
            int intY = (int) mouse.y-2;
            if ((intX >= camX + (i * 31) && intX <= camX + ((i+1) * 31)) && (intY >= camY && intY <= camY + 28)) {
                return i;
            }
        }
        return -1;
    }


    public Inventory getInv() {
        return inv;
    }
}