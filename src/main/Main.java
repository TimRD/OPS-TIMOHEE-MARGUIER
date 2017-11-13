
package main;
import java.util.*;
import java.lang.*;

/**
 *
 * @author timy
 */

public class Main
{
    public static Vector <OPS> init_T(Vector <OPS> T)//this method is to initialize the Vector by the user
    {
        int nbProcess = 0;
        Scanner sc = new Scanner(System.in);
        System.out.print("How much process do you want : ");
        nbProcess = sc.nextInt();

        for(int i = 0;i<nbProcess;i++)
        {
            OPS p = new OPS();
            p.setProcess(i);
            System.out.print("What is the arriTal : ");
            p.setArrival(sc.nextInt());
            System.out.print("What is the Burst Time : ");
            p.setBurst_Time(sc.nextInt());
            T.insertElementAt(p,i);
        }
        return T;
    }
    public static void Print(Vector <OPS> T)// this method permit to print the board calculated
    {
        float cpt1 = 0;
        int cpt =0;
        float m = T.size();
        System.out.println("Process | ArriTal |  Burst Time  | Start | Wait | Finish | TA");
        for(int i = 0;i<m;i++)
        {
            System.out.println(T.get(i).GetProcess() +"         "+T.get(i).GetArrival()  +
                    "           "+ T.get(i).GetBurst_Time() +"           " + T.get(i).GetStart()+"        "
                    +T.get(i).GetWait()+"      "+T.get(i).GetFinish()+"      "+T.get(i).GetTA()  );
            cpt += T.get(i).GetTA();
            cpt1+= T.get(i).GetWait();
        }
        System.out.println("\n\nATerage turnaround time is "+ cpt/m);
        System.out.println("ATerage waiting time is : "+cpt1/m);
    }
    public static void Calculate1_2_3(Vector <OPS> T)// this method calculate the board data for exercice 1,2 and 3
    {

        for (int i =0; i<T.size();i++)
        {
            T.get(i).setProcess(i);
            if(i == 0)
            {
                T.get(i).setStart(0);
                T.get(i).setWait(0);
            }
            else
            {
                T.get(i).setStart(T.get(i-1).GetFinish());

            }
            T.get(i).setFinish(T.get(i).GetStart()+T.get(i).GetBurst_Time());// it's where i calculate the waiting time, the finish and the turnaround time
            T.get(i).setTA(T.get(i).GetFinish() - T.get(i).GetArrival());//TA = Finish(Exit) - ArriTal time
            T.get(i).setWait(T.get(i).GetTA()-T.get(i).GetBurst_Time());//Waiting time = TurnaroudTime - BurstTime
            if(T.get(i).GetWait() <0) T.get(i).setWait(0);
        }
    }

    public static Vector <OPS> T_orderFCFS(Vector <OPS> T)// This function is to order by arriTal time
    {
        for(int j = 0;j<T.size();j++)
        {
            for(int k= 0;k<T.size();k++)
            {


                if(T.get(j).GetArrival()<=T.get(k).GetArrival())
                {
                    OPS p = new OPS();
                    p.setArrival(T.get(k).GetArrival());
                    T.get(k).setArrival(T.get(j).GetArrival());
                    T.get(j).setArrival(p.GetArrival());
                }
            }
        }

        return T;
    }

    public static Vector <OPS> T_orderSJF(Vector <OPS> T)// This function permit to put the smaller arriTal in first and then order by BurstTime
    {
        int cpt = 0;
        for(int j = 0;j<T.size();j++)
        {
            for(int k= 0;k<T.size();k++)
            {
                if(T.get(j).GetArrival()<=T.get(k).GetArrival())
                {
                    OPS p = new OPS();
                    p.setArrival(T.get(k).GetArrival());
                    p.setBurst_Time(T.get(k).GetBurst_Time());
                    T.get(k).setArrival(T.get(j).GetArrival());
                    T.get(k).setBurst_Time(T.get(j).GetBurst_Time());
                    T.get(j).setArrival(p.GetArrival());
                    T.get(j).setBurst_Time(p.GetBurst_Time());
                    cpt += T.get(j).GetBurst_Time();
                }
            }
        }

        for(int l = 1;l<T.size();l++)
        {
            for(int p = 1;p<T.size();p++)
            {
                if(T.get(l).GetArrival() <= cpt)
                {
                    if(T.get(l).GetBurst_Time()<= T.get(p).GetBurst_Time())
                    {
                        OPS q = new OPS();
                        q.setArrival(T.get(l).GetArrival());
                        q.setBurst_Time(T.get(l).GetBurst_Time());
                        T.get(l).setArrival(T.get(p).GetArrival());
                        T.get(l).setBurst_Time(T.get(p).GetBurst_Time());
                        T.get(p).setArrival(q.GetArrival());
                        T.get(p).setBurst_Time(q.GetBurst_Time());
                        cpt += T.get(l).GetBurst_Time();
                    }
                }
            }
        }

        return T;
    }

    public static void T_OrderSRTN(Vector <OPS> T,int Q)// This function (Exercice 3) use the order function of the Second exercice but with a quantum
    {
        int cpt = 0;
        int sz = T.size();
        int tmp;
        T_orderSJF(T);
        Calculate1_2_3(T);

        do {

            T.get(cpt).setBurst_Time(T.get(cpt).GetBurst_Time()-Q);// Burst Time = Burst TIme - Quantum

            if(T.get(cpt).GetBurst_Time()<=0)
            {

                tmp = Math.abs(T.get(cpt).GetBurst_Time());// We take the absolute Talue of BurstTime - Q
                T.get(cpt).setBurst_Time(0);
                cpt++;
                if(cpt == sz)
                {
                    System.out.println("Final Waiting time(The turnaround is already good) : ATerage waiting time is : "+(float)T.get(sz-1).GetFinish()/sz);
                    break;
                }
                T.get(cpt).setBurst_Time(T.get(cpt).GetBurst_Time()-tmp);
                Print(T);

            }


        }while(cpt != sz);


    }
    public static void T_OrderRR(Vector <OPS> T,int Q)
    {
        T_orderFCFS(T);
        int index = 0;
        int b = 0;
        int cpt = 0;
        int sz = T.size();
        int []countW = new int[sz];
        cpt++;
        do
        {
            if(T.get(index).GetFinish() != 0 || T.get(index).GetBurst_Time() == 0)
            {
                T.get(index).setWait(T.get(index).GetStart() - T.get(index).GetArrival()+countW[index]);
                index++;
                cpt++;
                System.out.println("I'm in the condition!"+index);
                Print(T);
                continue;
            }
            if(cpt == sz) break;
            if(T.get(index).GetStart() == 0)
            {
                if(index!=0)T.get(index).setStart(b);
            }
            T.get(index).setBurst_Time(T.get(index).GetBurst_Time()-Q);
            b = b+Q;
            if(T.get(index).GetBurst_Time()<=0)
            {

                T.get(index).setBurst_Time(0);
                T.get(index).setFinish(b);


            }
            countW[index]+= T.get(index).GetWait();

            index++;
            if(index == sz) {
                index = 0;
                cpt = 0;
            }


        }while(true);




    }


    public static void main(String[] args)
    {

        int choice;
        int Q =0;
        Vector <OPS> T = new  Vector<OPS>();
        Scanner sc = new Scanner(System.in);
        System.out.print("1.FCFS\n2.SJF\n3.SRTF\n4.RR\n5.Quit\n\nYour choice : ");
        choice = sc.nextInt();
        init_T(T);
        switch(choice)
        {
            case 1 ://FCFS
            {
                T_orderFCFS(T);
                Calculate1_2_3(T);
                Print(T);
                break;
            }
            case 2 ://SJF
            {

                T_orderSJF(T);
                Calculate1_2_3(T);
                Print(T);
                break;
            }
            case 3 ://SRTF
            {
                System.out.println("Select the Quantum : ");
                Q = sc.nextInt();
                T_OrderSRTN(T,Q);
                break;
            }
            case 4 ://RR
            {

                System.out.println("Select the Quantum : ");
                Q = sc.nextInt();
                T_OrderRR(T,Q);
                Print(T);
                break;
            }
            case 5 ://QUIT
            {
                System.out.println("Good bye!");
                break;
            }
            default :
            {
                System.out.print("No correct enter, please try again ! ");
                break;
            }
        }

    }

}
