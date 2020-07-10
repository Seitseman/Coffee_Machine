package machine;

import java.util.Scanner;

public class CoffeeMachine {

    public enum Coffee {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPUCCINO(200, 100, 12, 6);

        private final int water;
        private final int milk;
        private final int beans;
        private final int money;

        Coffee(int water, int milk, int beans, int money) {
            this.water = water;
            this.milk = milk;
            this.beans = beans;
            this.money = money;
        }

        public int getWater() {
            return water;
        }

        public int getMilk() {
            return milk;
        }

        public int getBeans() {
            return beans;
        }

        public int getMoney() {
            return money;
        }
    }

    private enum State {
        INIT("Write action (buy, fill, take, remaining, exit):"),
        FILL_WATER("Write how many ml of water do you want to add:"),
        FILL_MILK("Write how many ml of milk do you want to add:"),
        FILL_COFFEE("Write how many grams of coffee beans do you want to add:"),
        FILL_CUPS("Write how many disposable cups of coffee do you want to add:"),
        FILL_END("Write how many disposable cups of coffee do you want to add:"),
        BUY("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");

        private final String text;

        State(String text) {
            this.text = text;
        }

        public String getText() {
            return text;

        }

    }

    CoffeeMachine(int water, int milk, int coffee, int cups, int money) {
        this.curWater = water;
        this.curMilk = milk;
        this.curCoffee = coffee;
        this.curCups = cups;
        this.curMoney = money;

        curState = State.INIT;
    }

    Coffee curCoffeeType;
    State curState;

    int curWater;
    int curMilk;
    int curCoffee;
    int curCups;
    int curMoney;

    public void executeCommand(String command) {
        if (command.endsWith("remaining")) {
            printRemainedIngredients();
            curState = State.INIT;
            printCurStateText();
            return;
        } else if (command.endsWith("take")) {
            System.out.println("I gave you $" + curMoney);
            curMoney = 0;
            curState = State.INIT;
            printCurStateText();
            return;
        } else if (command.endsWith("fill")) {
            curState = State.FILL_WATER;
            printCurStateText();
            return;
        } else if (command.endsWith("buy")) {
            curState = State.BUY;
            printCurStateText();
            return;
        } else if (command.endsWith("back")) {
            curState = State.INIT;
            printCurStateText();
            return;
        }

        int quantity = Integer.parseInt(command);
        if (curState == State.FILL_WATER) {
            curWater += quantity;
            curState = State.FILL_MILK;
            printCurStateText();
        } else if (curState == State.FILL_MILK) {
            curMilk += quantity;
            curState = State.FILL_COFFEE;
            printCurStateText();
        } else if (curState == State.FILL_COFFEE) {
            curCoffee += quantity;
            curState = State.FILL_CUPS;
            printCurStateText();
        } else if (curState == State.FILL_CUPS) {
            curCups += quantity;
            curState = State.INIT;
            printCurStateText();
        } else if (curState == State.BUY) {
            tryPreparingCoffee(quantity);
        }
    }

    private  void tryPreparingCoffee(int code) {
        curCoffeeType = Coffee.values()[code - 1];
        if (curWater < curCoffeeType.getWater()) {
            System.out.println("Sorry, not enough water!");
            return;
        }

        if (curMilk < curCoffeeType.getMilk()) {
            System.out.println("Sorry, not enough milk!");
            return;
        }

        if (curCoffee < curCoffeeType.getBeans()) {
            System.out.println("Sorry, not enough coffee!");
            return;
        }

        if (curCups < 1) {
            System.out.println("Sorry, not enough cups!");
            return;
        }

        curWater -= curCoffeeType.getWater();
        curMilk -= curCoffeeType.getMilk();
        curCoffee -= curCoffeeType.getBeans();
        curCups--;
        curMoney += curCoffeeType.getMoney();

        System.out.println("I have enough resources, making you a coffee!");
    }

    private void printCurStateText() {
        System.out.println(curState.getText());
    }

    private void printRemainedIngredients() {
        System.out.println("The coffee machine has:");
        System.out.format("%d of water%n", curWater);
        System.out.format("%d of milk%n", curMilk);
        System.out.format("%d of coffee beans%n", curCoffee);
        System.out.format("%d of disposable cups%n", curCups);
        System.out.format("%d of money%n", curMoney);
    }

    public static void main(String[] args) {
        CoffeeMachine machine = new CoffeeMachine(400, 540, 120, 9, 550);
        Scanner scanner = new Scanner(System.in);

        machine.executeCommand("back");
        String action = scanner.nextLine();

        while (!action.endsWith("exit")) {
            machine.executeCommand(action);
            action = scanner.nextLine();
        }

    }
}
