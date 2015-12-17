package io.github.anti_java;

import io.github.anti_java.exceptions.InvalidCharacterException;
import io.github.anti_java.exceptions.RepeatedCharacterException;
import io.github.anti_java.interfaces.CentralizedDataCenter;
import io.github.anti_java.struct.Character;
import io.github.anti_java.struct.Item;
import io.github.anti_java.struct.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Davy on 2015/12/17.
 */
public class CDC implements CentralizedDataCenter, Global {
    private boolean mRunningThread = false;
    private final HashMap<Integer, Character> mCharacters;
    private final HashMap<Position, Item> mItems;
    private final ArrayList<Object> mUpdates;
    private Thread mUpdatingThread = null;

    public CDC() {
        mCharacters = new HashMap<>();
        mItems = new HashMap<>();
        mUpdates = new ArrayList<>();
    }

    @Override
    public void addVirtualCharacter(final int clientNo)
            throws RepeatedCharacterException {
        if (mCharacters.containsKey(clientNo))
            throw new RepeatedCharacterException(clientNo);

        final Character character = new Character();
        mCharacters.put(clientNo, character);
        mUpdates.add(character);
    }

    @Override
    public void addItem(final String name, final int index, final boolean shared,
                        final int x, final int y) {
        final Item item = new Item(name, index, shared);
        mItems.put(new Position(x, y), item);
        mUpdates.add(item);
    }

    @Override
    public void updateDirection(final int clientNo, final int moveCode) throws InvalidCharacterException {
        if (!mCharacters.containsKey(clientNo))
            throw new InvalidCharacterException(clientNo);

        final Character character = mCharacters.get(clientNo);
        switch (moveCode) {
            case TURNEAST: {
                character.direction = EAST;
                break;
            }
            case TURNNORTH: {
                character.direction = NORTH;
                break;
            }
            case TURNSOUTH: {
                character.direction = SOUTH;
                break;
            }
            case TURNWEST: {
                character.direction = WEST;
                break;
            }
            default: {
                return;
            }
        }
        mUpdates.add(character);
    }

    @Override
    public void getItem(int clientNo) throws InvalidCharacterException {
        if (!mCharacters.containsKey(clientNo))
            throw new InvalidCharacterException(clientNo);

        final Character character = mCharacters.get(clientNo);
        final Position itemPosition = getCharacterFacedItemPosition(character);
        if (itemPosition == null)
            return;

        if (mItems.containsKey(itemPosition)) {
            final Item item = mItems.get(itemPosition);
            if (item.isShared() && item.isReached)
                return;
            item.isReached = item.isShared();
            character.items.add(item);

            mUpdates.add(item);
            mUpdates.add(character);
        }
    }

    @Override
    public List<Object> getUpdateInfo() {
        final List<Object> updates = new ArrayList<>(mUpdates);
        mUpdates.clear();
        return updates;
    }

    @Override
    public void startUpdatingThread() {
        if (mUpdatingThread != null) {
            mRunningThread = false;
            mUpdatingThread.interrupt();
            mUpdatingThread = null;
        }

        mUpdatingThread = new Thread(() -> {
            try {
                while (mRunningThread) {
                    Thread.sleep(500);
                    updateCharactersPosition();
                }
            } catch (InterruptedException e) {
                // do nothing
            }
        });
        mRunningThread = true;
        mUpdatingThread.start();
    }

    private void updateCharactersPosition() {
        for (Character character : mCharacters.values()) {
            final Position oldPosition = new Position(character.position.x, character.position.y);
            switch (character.direction) {
                case EAST: {
                    character.position.x += character.velocity;
                    break;
                }
                case NORTH: {
                    character.position.y -= character.velocity;
                    break;
                }
                case SOUTH: {
                    character.position.y += character.velocity;
                    break;
                }
                case WEST: {
                    character.position.x -= character.velocity;
                    break;
                }
            }
            if (character.position.x != oldPosition.x || character.position.y != oldPosition.y)
                mUpdates.add(character);
        }
    }

    private Position getCharacterFacedItemPosition(final Character character) {
        final Position userPosition = character.position;
        final Position itemPosition;
        switch (character.direction) {
            case EAST: {
                itemPosition = new Position(userPosition.x + 1, userPosition.y);
                break;
            }
            case NORTH: {
                itemPosition = new Position(userPosition.x, userPosition.y - 1);
                break;
            }
            case SOUTH: {
                itemPosition = new Position(userPosition.x, userPosition.y + 1);
                break;
            }
            case WEST: {
                itemPosition = new Position(userPosition.x - 1, userPosition.y);
                break;
            }
            default: {
                return null;
            }
        }

        return itemPosition;
    }
}
