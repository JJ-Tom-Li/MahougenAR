package com.vuforia.MahougenAR.SampleApplication.utils;

import java.nio.Buffer;

public class Mahougen extends MeshObject{
    private Buffer mVertBuff;
    private Buffer mTexCoordBuff;
    private Buffer mNormBuff;
    private Buffer mIndBuff;

    private int indicesNumber = 0;
    private int verticesNumber = 0;

    public Mahougen()
    {
        setVerts();
        setTexCoords();
        setNorms();
        setIndices();
    }

    int bananaNumVerts = 24168;
    private void setIndices(){
        short[] indice = new short[]{ 0, 1, 2, 3};
        mIndBuff = fillBuffer(indice);
        indicesNumber = indice.length;
    }
    private void setVerts(){
        //double[] verts = new double[] {};

        double[] verts = new double[] {
                -0.5f, -0.5f, 0.0f, //bottom-left corner
                0.5f, -0.5f, 0.0f, //bottom-right corner
                0.5f,  0.5f, 0.0f, //top-right corner
                -0.5f,  0.5f, 0.0f  //top-left corner
        };
        mVertBuff = fillBuffer(verts);
        verticesNumber = verts.length / 3;
    }
    private void setNorms(){
        double[] norms = new double []{
                0.0f, 0.0f, 1.0f, //normal at bottom-left corner
                0.0f, 0.0f, 1.0f, //normal at bottom-right corner
                0.0f, 0.0f, 1.0f, //normal at top-right corner
                0.0f, 0.0f, 1.0f  //normal at top-left corner
        };
        mNormBuff = fillBuffer(norms);
    }
    private void setTexCoords(){
        double[] cords = new double[]{
                0.0f, 0.0f, //tex-coords at bottom-left corner
                1.0f, 0.0f, //tex-coords at bottom-right corner
                1.0f, 1.0f, //tex-coords at top-right corner
                0.0f, 1.0f  //tex-coords at top-left corner
        };
        mTexCoordBuff = fillBuffer(cords);

    }
    public int getNumObjectIndex()
    {
        return indicesNumber;
    }


    @Override
    public int getNumObjectVertex()
    {
        return verticesNumber;
    }


    @Override
    public Buffer getBuffer(BUFFER_TYPE bufferType)
    {
        Buffer result = null;
        switch (bufferType)
        {
            case BUFFER_TYPE_VERTEX:
                result = mVertBuff;
                break;
            case BUFFER_TYPE_TEXTURE_COORD:
                result = mTexCoordBuff;
                break;
            case BUFFER_TYPE_NORMALS:
                result = mNormBuff;
                break;
            case BUFFER_TYPE_INDICES:
                result = mIndBuff;
            default:
                break;

        }

        return result;
    }
}
