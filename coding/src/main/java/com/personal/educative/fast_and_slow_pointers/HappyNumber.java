package com.personal.educative.fast_and_slow_pointers;

public class HappyNumber {
    public boolean isHappy(int n) {
        int n1=n,n2=n;
        while(true){
            n1 = next(n1);
            n2 = next(next(n2));
            if(n1==1 || n2==1) break;
            if(n1==n2)
                return false;
        }
        return true;
    }
    public int next(int n){
        int sum = 0;
        while(n/10!=0){
            sum+= (n%10)*(n%10);
            n = n/10;
        }
        sum+= (n%10)*(n%10);
        return sum;
    }
}
