import functions.*;
import functions.basic.*;
import functions.meta.Composition;

import java.io.*;

public class Task9 {

    public static void main(String[] args) {
        try {
            Function f = new Composition(new Log(Math.E), new Exp());


            System.out.println("функция ln(exp(x)) = x на [0, 10] с шагом 1\n");
            TabulatedFunction original = TabulatedFunctions.tabulate(f, 0, 10, 11);

            System.out.println("Serializable");
            File serialFile = new File("serializable.bin");

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serialFile))) {
                out.writeObject(original);
            }

            TabulatedFunction deserializedSerializable;
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(serialFile))) {
                deserializedSerializable = (TabulatedFunction) in.readObject();
            }

            System.out.println("Сравнение значений:");
            System.out.println("x\t| original\t| deserialized");
            for (int x = 0; x <= 10; x++) {
                System.out.printf("%d\t| %.6f\t| %.6f%n",
                        x, original.getFunctionValue(x), deserializedSerializable.getFunctionValue(x));
            }
            System.out.println();

            System.out.println("Externalizable");

            FunctionPoint[] points = new FunctionPoint[original.getPointsCount()];
            for (int i = 0; i < points.length; i++) {
                points[i] = new FunctionPoint(original.getPointX(i), original.getPointY(i));
            }
            TabulatedFunction externalFunction = new ArrayTabulatedFunctionExternal(points);

            File extFile = new File("externalizable.bin");

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(extFile))) {
                out.writeObject(externalFunction);
            }

            TabulatedFunction deserializedExternal;
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(extFile))) {
                deserializedExternal = (TabulatedFunction) in.readObject();
            }

            System.out.println("Сравнение значений:");
            System.out.println("x\t| original\t| deserialized");
            for (int x = 0; x <= 10; x++) {
                System.out.printf("%d\t| %.6f\t| %.6f%n",
                        x, original.getFunctionValue(x), deserializedExternal.getFunctionValue(x));
            }

            serialFile.delete();
            extFile.delete();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}