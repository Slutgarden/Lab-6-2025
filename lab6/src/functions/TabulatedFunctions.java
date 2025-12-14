package functions;
import java.io.*;
import java.util.StringJoiner;

public class TabulatedFunctions {

    private TabulatedFunctions(){}

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException{
        DataOutputStream dataOut = new DataOutputStream(out);

        int count = function.getPointsCount();
        dataOut.writeInt(count);
        for(int i = 0; i < count; i++){
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException{
        DataInputStream dataIn = new DataInputStream(in);

        int count = dataIn.readInt();
        if(count < 2){
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        FunctionPoint[] points = new FunctionPoint[count];
        for(int i = 0; i < count; i++){
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        int count = function.getPointsCount();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(String.valueOf(count));
        for(int i = 0; i < count; i++){
            joiner.add(String.valueOf(function.getPointX(i)));
            joiner.add(String.valueOf(function.getPointY(i)));
        }
        out.write(joiner.toString());
        out.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        tokenizer.nextToken();
        int count = (int) tokenizer.nval;
        if(count < 2){
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        FunctionPoint[] points = new FunctionPoint[count];
        for(int i = 0; i < count; i++){
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(points);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if(leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException("границы для табулирования выходят за область определения функции");
        }
        if(pointsCount < 2){
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
}