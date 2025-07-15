package shdShadow.BicoccaOrganizer.Interfaces;

import java.io.IOException;

public interface IJsonIO<R, I>{
    public R saveToJson(I toBeSerialized) throws IOException;
    public I loadFromJson();
}