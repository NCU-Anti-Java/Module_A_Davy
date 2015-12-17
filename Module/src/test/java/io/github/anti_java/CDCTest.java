package io.github.anti_java;

import io.github.anti_java.exceptions.InvalidCharacterException;
import io.github.anti_java.exceptions.RepeatedCharacterException;
import io.github.anti_java.struct.Character;
import io.github.anti_java.struct.Item;
import io.github.anti_java.struct.Position;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Davy on 2015/12/17.
 */
public class CDCTest {
    static final int stubClientNo = 0;
    static final Item stubItem = new Item("JUnit", 0, true);
    static final Item stubItem2 = new Item("JUnit2", 99, false);
    static final Position stubPosition = new Position(0, 99);
    static final List<Pair<Integer, Integer>> letMoveCodeDirections = new ArrayList<>();
    static {
        letMoveCodeDirections.add(new Pair<>(Global.TURNNORTH, Global.NORTH));
        letMoveCodeDirections.add(new Pair<>(Global.TURNEAST, Global.EAST));
        letMoveCodeDirections.add(new Pair<>(Global.TURNSOUTH, Global.SOUTH));
        letMoveCodeDirections.add(new Pair<>(Global.TURNWEST, Global.WEST));
    }
    static final int stubVelocity = 1;

    @Test
    public void successAddVirtualCharacterIntoCharacterList()
            throws RepeatedCharacterException, NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();

        cdc.addVirtualCharacter(stubClientNo);

        final Field characterListField = CDC.class.getDeclaredField("mCharacters");
        characterListField.setAccessible(true);
        final HashMap<Integer, Character> characters = (HashMap<Integer, Character>) characterListField.get(cdc);
        Assert.assertTrue(characters.containsKey(stubClientNo));
    }

    @Test(expected = RepeatedCharacterException.class)
    public void throwExceptionWhenAddRepeatedVirtualCharacter()
            throws RepeatedCharacterException, NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        insertStubVirtualCharacter(cdc);

        cdc.addVirtualCharacter(stubClientNo);
    }

    @Test
    public void successAddItemIntoItemList()
            throws NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();

        cdc.addItem(stubItem.getName(), stubItem.getId(), stubItem.isShared(), stubPosition.x, stubPosition.y);

        final Field itemListField = CDC.class.getDeclaredField("mItems");
        itemListField.setAccessible(true);
        final HashMap<Position, Item> items = (HashMap<Position, Item>) itemListField.get(cdc);
        Assert.assertTrue(items.containsKey(stubPosition));

        final Item item = items.get(stubPosition);
        Assert.assertEquals(item.getName(), stubItem.getName());
        Assert.assertEquals(item.getId(), stubItem.getId());
        Assert.assertEquals(item.isShared(), stubItem.isShared());
    }

    @Test
    public void replaceItemIntoItemListWhenAddToSamePosition()
            throws NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        insertStubItem(cdc);

        cdc.addItem(stubItem2.getName(), stubItem2.getId(), stubItem2.isShared(), stubPosition.x, stubPosition.y);

        final Field itemListField = CDC.class.getDeclaredField("mItems");
        itemListField.setAccessible(true);
        final HashMap<Position, Item> items = (HashMap<Position, Item>) itemListField.get(cdc);
        final Item item = items.get(stubPosition);
        Assert.assertTrue(item.getName().equals(stubItem2.getName()));
        Assert.assertEquals(item.getId(), stubItem2.getId());
        Assert.assertEquals(item.isShared(), stubItem2.isShared());
    }

    @Test(expected = InvalidCharacterException.class)
    public void throwWhenUpdateDirectionWithUnknownClientNo()
            throws InvalidCharacterException {
        final CDC cdc = new CDC();

        cdc.updateDirection(stubClientNo, letMoveCodeDirections.get(0).getKey());
    }

    @Test
    public void successUpdateDirection()
            throws InvalidCharacterException, NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        insertStubVirtualCharacter(cdc);

        for (Pair<Integer, Integer> p : letMoveCodeDirections) {
            cdc.updateDirection(stubClientNo, p.getKey());

            final Field characterListField = CDC.class.getDeclaredField("mCharacters");
            characterListField.setAccessible(true);
            final HashMap<Integer, Character> characters = (HashMap<Integer, Character>) characterListField.get(cdc);
            final Character character = characters.get(stubClientNo);
            Assert.assertEquals(character.direction, (int) p.getValue());
        }
    }

    @Test(expected = InvalidCharacterException.class)
    public void throwWhenGetItemWithUnknownClientNo()
            throws InvalidCharacterException {
        final CDC cdc = new CDC();

        cdc.getItem(stubClientNo);
    }

    @Test
    public void successGetItem()
            throws InvalidCharacterException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Character character = insertStubVirtualCharacter(cdc);
        character.position.x = stubPosition.x;
        character.position.y = stubPosition.y;

        final Field itemListField = CDC.class.getDeclaredField("mItems");
        itemListField.setAccessible(true);
        final HashMap<Position, Item> items = (HashMap<Position, Item>) itemListField.get(cdc);
        // directions
        {
            final Position position = new Position(stubPosition.x + 1, stubPosition.y);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), false);
            items.put(position, item);
            character.direction = Global.EAST;

            cdc.getItem(stubClientNo);

            Assert.assertTrue(character.items.contains(item));
            character.items.clear();
        }
        {
            final Position position = new Position(stubPosition.x - 1, stubPosition.y);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), false);
            items.put(position, item);
            character.direction = Global.WEST;

            cdc.getItem(stubClientNo);

            Assert.assertTrue(character.items.contains(item));
            character.items.clear();
        }
        {
            final Position position = new Position(stubPosition.x, stubPosition.y + 1);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), false);
            items.put(position, item);
            character.direction = Global.SOUTH;

            cdc.getItem(stubClientNo);

            Assert.assertTrue(character.items.contains(item));
            character.items.clear();
        }
        {
            final Position position = new Position(stubPosition.x, stubPosition.y - 1);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), false);
            items.put(position, item);
            character.direction = Global.NORTH;

            cdc.getItem(stubClientNo);

            Assert.assertTrue(character.items.contains(item));
            character.items.clear();
        }

        // shared && reached
        {
            final Position position = new Position(stubPosition.x + 1, stubPosition.y);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), true);
            item.isReached = true;
            items.put(position, item);
            character.direction = Global.EAST;

            cdc.getItem(stubClientNo);

            Assert.assertFalse(character.items.contains(item));
            character.items.clear();
        }
        // shared && un-reached
        {
            final Position position = new Position(stubPosition.x + 1, stubPosition.y);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), true);
            items.put(position, item);
            character.direction = Global.EAST;

            cdc.getItem(stubClientNo);

            Assert.assertTrue(character.items.contains(item));
            Assert.assertTrue(item.isReached);
            character.items.clear();
        }
        // non-shared
        {
            final Position position = new Position(stubPosition.x + 1, stubPosition.y);
            final Item item = new Item(stubItem.getName(), stubItem.getId(), false);
            items.put(position, item);
            character.direction = Global.EAST;

            cdc.getItem(stubClientNo);

            Assert.assertTrue(character.items.contains(item));
            Assert.assertFalse(item.isReached);
            character.items.clear();
        }
    }

    @Test
    public void successRecordUpdateInfosWhenAddVirtaulCharacter()
            throws RepeatedCharacterException, NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Field updateListField = CDC.class.getDeclaredField("mUpdates");
        updateListField.setAccessible(true);
        final List<Object> updateList = (List<Object>) updateListField.get(cdc);
        final Field characterListField = CDC.class.getDeclaredField("mCharacters");
        characterListField.setAccessible(true);
        final HashMap<Integer, Character> characters = (HashMap<Integer, Character>) characterListField.get(cdc);

        cdc.addVirtualCharacter(stubClientNo);
        Assert.assertTrue(updateList.size() == 1);
        final Object data = updateList.get(0);
        Assert.assertTrue(data instanceof Character);
        final Character character = (Character) data;
        Assert.assertEquals(character, characters.get(stubClientNo));
    }

    @Test
    public void successRecordUpdateInfosWhenUpdateDirection()
            throws InvalidCharacterException, NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Field updateListField = CDC.class.getDeclaredField("mUpdates");
        updateListField.setAccessible(true);
        final List<Object> updateList = (List<Object>) updateListField.get(cdc);

        final Character stubCharacter = insertStubVirtualCharacter(cdc);

        cdc.updateDirection(stubClientNo, Global.DEFAULT_DIR);
        Assert.assertTrue(updateList.size() == 1);
        final Object data = updateList.get(0);
        Assert.assertTrue(data instanceof Character);
        final Character character = (Character) data;
        Assert.assertEquals(character, stubCharacter);
    }

    @Test
    public void successRecordUpdateInfosWhenAddItem()
            throws RepeatedCharacterException, InvalidCharacterException,
            NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Field updateListField = CDC.class.getDeclaredField("mUpdates");
        updateListField.setAccessible(true);
        final List<Object> updateList = (List<Object>) updateListField.get(cdc);
        final Field itemListField = CDC.class.getDeclaredField("mItems");
        itemListField.setAccessible(true);
        final HashMap<Position, Item> items = (HashMap<Position, Item>) itemListField.get(cdc);

        cdc.addItem(stubItem.getName(), stubItem.getId(), stubItem.isShared(), stubPosition.x, stubPosition.y);
        Assert.assertTrue(updateList.size() == 1);
        final Object data = updateList.get(0);
        Assert.assertTrue(data instanceof Item);
        final Item item = (Item) data;
        Assert.assertEquals(item, items.get(stubPosition));
    }

    @Test
    public void successRecordUpdateInfosWhenGetItem()
            throws RepeatedCharacterException, InvalidCharacterException,
            NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Field updateListField = CDC.class.getDeclaredField("mUpdates");
        updateListField.setAccessible(true);
        final List<Object> updateList = (List<Object>) updateListField.get(cdc);
        final Field itemListField = CDC.class.getDeclaredField("mItems");
        itemListField.setAccessible(true);
        final HashMap<Position, Item> items = (HashMap<Position, Item>) itemListField.get(cdc);

        final Character character = insertStubVirtualCharacter(cdc);
        character.position.x = stubPosition.x;
        character.position.y = stubPosition.y;
        character.direction = Global.EAST;
        final Position itemPosition = new Position(stubPosition.x + 1, stubPosition.y);
        final Item item = new Item(stubItem.getName(), stubItem.getId(), false);
        items.put(itemPosition, item);

        cdc.getItem(stubClientNo);
        Assert.assertTrue(updateList.size() == 2);
        {
            final Object data = updateList.get(0);
            Assert.assertTrue(data instanceof Item);
            final Item dataItem = (Item) data;
            Assert.assertEquals(dataItem, item);
        }
        {
            final Object data = updateList.get(1);
            Assert.assertTrue(data instanceof Character);
            final Character dataCharacter = (Character) data;
            Assert.assertEquals(dataCharacter, character);
        }
    }

    @Test
    public void successGetUpdateInfo()
            throws NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Field updateListField = CDC.class.getDeclaredField("mUpdates");
        updateListField.setAccessible(true);
        final List<Object> updateList = (List<Object>) updateListField.get(cdc);

        updateList.add(stubPosition);
        updateList.add(stubItem);

        final List<Object> gotUpdateList = cdc.getUpdateInfo();
        Assert.assertEquals(gotUpdateList.get(0), stubPosition);
        Assert.assertEquals(gotUpdateList.get(1), stubItem);

        Assert.assertTrue(cdc.getUpdateInfo().size() == 0);
    }

    @Test
    public void successStartUpdatingThread()
            throws NoSuchFieldException, IllegalAccessException {
        final CDC cdc = new CDC();
        final Field updatingThreadField = CDC.class.getDeclaredField("mUpdatingThread");
        updatingThreadField.setAccessible(true);
        final Field runningThreadField = CDC.class.getDeclaredField("mRunningThread");
        runningThreadField.setAccessible(true);

        cdc.startUpdatingThread();
        final Thread updatingThread = (Thread) updatingThreadField.get(cdc);
        Assert.assertTrue(updatingThread != null);

        runningThreadField.set(cdc, false);
        updatingThread.interrupt();
    }

    @Test
    public void successUpdateCharactersPosition()
            throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
                IllegalArgumentException, InvocationTargetException {
        final CDC cdc = new CDC();
        final Character character = insertStubVirtualCharacter(cdc);
        character.velocity = stubVelocity;
        final Method updateCharactersPositionMethod = CDC.class.getDeclaredMethod("updateCharactersPosition");
        updateCharactersPositionMethod.setAccessible(true);

        {
            character.position.x = stubPosition.x;
            character.position.y = stubPosition.y;
            character.direction = Global.EAST;
            updateCharactersPositionMethod.invoke(cdc);
            Assert.assertEquals(character.position.x, stubPosition.x + stubVelocity);
            Assert.assertEquals(character.position.y, stubPosition.y);
        }
        {
            character.position.x = stubPosition.x;
            character.position.y = stubPosition.y;
            character.direction = Global.WEST;
            updateCharactersPositionMethod.invoke(cdc);
            Assert.assertEquals(character.position.x, stubPosition.x - stubVelocity);
            Assert.assertEquals(character.position.y, stubPosition.y);
        }
        {
            character.position.x = stubPosition.x;
            character.position.y = stubPosition.y;
            character.direction = Global.NORTH;
            updateCharactersPositionMethod.invoke(cdc);
            Assert.assertEquals(character.position.x, stubPosition.x);
            Assert.assertEquals(character.position.y, stubPosition.y - stubVelocity);
        }
        {
            character.position.x = stubPosition.x;
            character.position.y = stubPosition.y;
            character.direction = Global.SOUTH;
            updateCharactersPositionMethod.invoke(cdc);
            Assert.assertEquals(character.position.x, stubPosition.x);
            Assert.assertEquals(character.position.y, stubPosition.y + stubVelocity);
        }
    }

    @Test
    public void successRecordUpdateInfosWhenUpdateCharactersPosition()
            throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
                IllegalArgumentException, InvocationTargetException {
        final CDC cdc = new CDC();
        final Field updateListField = CDC.class.getDeclaredField("mUpdates");
        updateListField.setAccessible(true);
        final List<Object> updateList = (List<Object>) updateListField.get(cdc);
        final Character character = insertStubVirtualCharacter(cdc);
        character.position.x = stubPosition.x;
        character.position.y = stubPosition.y;
        character.velocity = stubVelocity;
        character.direction = Global.EAST;
        final Method updateCharactersPositionMethod = CDC.class.getDeclaredMethod("updateCharactersPosition");
        updateCharactersPositionMethod.setAccessible(true);

        updateCharactersPositionMethod.invoke(cdc);
        Assert.assertTrue(updateList.size() == 1);
        final Object data = updateList.get(0);
        Assert.assertTrue(data instanceof Character);
        final Character dataCharacter = (Character) data;
        Assert.assertEquals(dataCharacter, character);
    }

    private Character insertStubVirtualCharacter(final CDC cdc)
            throws NoSuchFieldException, IllegalAccessException {
        final Field characterListField = CDC.class.getDeclaredField("mCharacters");
        characterListField.setAccessible(true);
        final HashMap<Integer, Character> characters = (HashMap<Integer, Character>) characterListField.get(cdc);
        final Character character = new Character();
        characters.put(stubClientNo, character);
        return character;
    }

    private Item insertStubItem(final CDC cdc)
            throws NoSuchFieldException, IllegalAccessException {
        final Field itemListField = CDC.class.getDeclaredField("mItems");
        itemListField.setAccessible(true);
        final HashMap<Position, Item> items = (HashMap<Position, Item>) itemListField.get(cdc);
        final Item item = new Item(stubItem.getName(), stubItem.getId(), stubItem.isShared());
        items.put(stubPosition, item);
        return item;
    }
}
