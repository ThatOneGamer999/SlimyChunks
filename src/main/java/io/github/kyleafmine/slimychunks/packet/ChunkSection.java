package io.github.kyleafmine.slimychunks.packet;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ChunkSection {
    private short blockCount;
    private static final byte BITS_PER_BLOCK = 8;
    private Palette palette;
    private byte[] blocks;
    int yIndex;
    public int decoded_bpb;

    public void setBlocks(byte[] blocks) {
        this.blocks = blocks;
    }

    public void setBlockCount(short blockCount) {
        this.blockCount = blockCount;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public ChunkSection(int i) {
        blockCount = 0;
        yIndex = i;
        palette = new Palette();
        blocks = new byte[4096];
        Arrays.fill(blocks, (byte) 0);
    }

    public int index(int x, int y, int z) {
        if (x < 0 || z < 0 || x >= 16 || z >= 16) {
            throw new IndexOutOfBoundsException(
                    "Coords (x=" + x + ",z=" + z + ") out of section bounds");
        }
        return (y * 256) + (z * 16) + x;
    }

    public void setBlock(int x, int y, int z, int material) {
        int m = palette.getOrSetMapping(material);
        blocks[index(x, y, z)] = (byte) m;
        if (m != 0) {
            blockCount++;
        }
    }
    public int getBlock(int x, int y, int z) {
        return palette.ids.get(blocks[index(x, y, z)]);
    }
    public byte[][] splitBytes(final byte[] data, final int chunkSize)
    {
        final int length = data.length;
        final byte[][] dest = new byte[(length + chunkSize - 1)/chunkSize][];
        int destIndex = 0;
        int stopIndex = 0;

        for (int startIndex = 0; startIndex + chunkSize <= length; startIndex += chunkSize)
        {
            stopIndex += chunkSize;
            dest[destIndex++] = Arrays.copyOfRange(data, startIndex, stopIndex);
        }

        if (stopIndex < length)
            dest[destIndex] = Arrays.copyOfRange(data, stopIndex, length);

        return dest;
    }
    public void fill(int material) {
        int m = palette.getOrSetMapping(material);
        Arrays.fill(blocks, (byte) m);
    }
    public byte[] toBytes() {
        byte[][] be = splitBytes(blocks, 8);
        ByteBuffer bb = ByteBuffer.allocate(4096);
        for (byte[] a : be) {
            ArrayUtils.reverse(a);
            bb.put(a);
        }
        bb.order(ByteOrder.BIG_ENDIAN);
        byte[] b = bb.array();


        int size = 2; // start at 2 because of 2 byte short blockCount;
        size ++; // add one byte (BitsPerBlock)
        size += palette.toBytes().length; // size of palette + palette size data
        size += VarInt.varIntSize(512); // length of block chunk
        size += b.length; // length of blocks array

        ByteBuffer buf = ByteBuffer.wrap(new byte[size]);
        buf.putShort(blockCount);
        buf.put((byte) 8);
        buf.put(palette.toBytes());
        VarInt.putVarInt(512, buf);



        buf.put(b);

        return buf.array();
    }
}
