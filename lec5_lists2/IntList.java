package lists2;

public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	/** Return the size of the list using... recursion! */
	public int size() {
		if (rest == null) {
			return 1;
		}
		return 1 + this.rest.size();
	}

	/** Return the size of the list using no recursion! */
	public int iterativeSize() {
		IntList p = this;
		int totalSize = 0;
		while (p != null) {
			totalSize += 1;
			p = p.rest;
		}
		return totalSize;
	}

	/** Returns the ith item of this IntList. */
	public int get(int i) {
		if (i == 0) {
			return first;
		}
		return rest.get(i - 1);
	}

	public void addAdjacent() {
		addAdjacentHelper(this);
	}

	private void addAdjacentHelper(IntList L) {
		if (rest != null) {
			if (first == rest.first) {
				first = first + rest.first;
				rest = rest.rest;
				L.addAdjacentHelper(L);
			} else {
				rest.addAdjacentHelper(L);
			}
		}
	}

	public static void main(String[] args) {
		// 辅助方法：快速判断链表内容是否符合预期
		// 这里的 check 函数模拟了 assertEquals 的功能

		// 1. 连续合并导致链表消失：8 -> 8 -> 8 -> 8 -> 8 -> 8 -> 8 -> 8 -> null
		IntList L1 = of(8, 8, 8, 8, 8, 8, 8, 8);
		L1.addAdjacent();
		check("Test 1 (Power of 2)", L1.first == 64 && L1.rest == null);

		// 2. 只有两个元素但不相等：1 -> 2
		IntList L2 = of(1, 2);
		L2.addAdjacent();
		check("Test 2 (Two unequal)", L2.first == 1 && L2.rest.first == 2);

		// 3. 只有两个元素且相等：2 -> 2
		IntList L3 = of(2, 2);
		L3.addAdjacent();
		check("Test 3 (Two equal)", L3.first == 4 && L3.rest == null);

		// 4. 单元素链表：5 -> null
		IntList L4 = of(5);
		L4.addAdjacent();
		check("Test 4 (Single element)", L4.first == 5 && L4.rest == null);

		// 5. 倒序合并触发：4 -> 2 -> 2 -> null (应变为 8)
		IntList L5 = of(4, 2, 2);
		L5.addAdjacent();
		check("Test 5 (Reverse merge)", L5.first == 8 && L5.rest == null);

		// 6. 夹心合并：2 -> 2 -> 3 -> 3 -> null (应变为 4 -> 6)
		IntList L6 = of(2, 2, 3, 3);
		L6.addAdjacent();
		check("Test 6 (Sandwich)", L6.first == 4 && L6.rest.first == 6);

		// 7. 大数在前：1024 -> 512 -> 512 -> null (应变为 2048)
		IntList L7 = of(1024, 512, 512);
		L7.addAdjacent();
		check("Test 7 (Large number)", L7.first == 2048 && L7.rest == null);

		// 8. 零元素处理（如果你的 first 支持 0）：0 -> 0 -> 7
		IntList L8 = of(0, 0, 7);
		L8.addAdjacent();
		check("Test 8 (Zeroes)", L8.first == 0 && L8.rest.first == 7);

		// 9. 锯齿形不合并：1 -> 2 -> 1 -> 2 -> 1
		IntList L9 = of(1, 2, 1, 2, 1);
		L9.addAdjacent();
		check("Test 9 (Sawtooth)", L9.iterativeSize() == 5);

		// 10. 超长链表末尾合并：1 -> 2 -> 3 -> ... -> 100 -> 100
		IntList L10 = of(1, 2, 3, 4, 5, 5);
		L10.addAdjacent();
		check("Test 10 (End merge)", L10.get(4) == 10);

		System.out.println("恭喜！所有极端测试通过！🎉");
	}

// ---------------- 辅助工具函数 ----------------

	public static void check(String label, boolean condition) {
		if (!condition) {
			throw new RuntimeException(label + " 失败了！检查一下指针逻辑。");
		}
	}

	/** 快速生成链表的静态方法 */
	public static IntList of(int... args) {
		IntList head = null;
		for (int i = args.length - 1; i >= 0; i--) {
			head = new IntList(args[i], head);
		}
		return head;
	}
} 