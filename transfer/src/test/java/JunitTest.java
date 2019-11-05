import org.junit.Test;

public class JunitTest {
    @Test
    public void t() {
        /*
         *借贷记帐法是以“借”、“贷”作为记帐符号的一种复式记帐法 有借必有贷，借贷必相等
         * 本题只针对中间账户记账
         */
        //期初借方余额
        Double beforeDebitBalance = 0D;
        //期初贷方余额
        Double beforeCreditBalance = 100D;
        System.out.println("期初借方余额:" + beforeDebitBalance);
        System.out.println("期初贷方余额:" + beforeCreditBalance);
        //借方发生额 为借方金额相加
        Double debitAmount = 100D;
        //贷方发生额 为贷方金额相加
        Double creditAmount = 50D;
        System.out.println("贷方发生额:" + creditAmount);
        System.out.println("借方发生额:" + debitAmount);
        //期末借方余额 为借方余额加+借方发生额
        Double afterDebitBalance = beforeDebitBalance + debitAmount;
        //期末贷方余额 为贷方余额+贷方发生额
        Double afterCreditBalance = beforeCreditBalance + creditAmount;
        System.out.println("期末借方余额:" + afterDebitBalance);
        System.out.println("期末贷方余额:" + afterCreditBalance);
    }
}
