package yuheng.lp.solver;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;

public class MLIPResult {
	private MLIPIterationNode root;
	private DecimalFormat df;
	private MLIPTermination status;
	private MLIPIterationNode optimal;

	public MLIPResult(MLIPIterationNode root) {
		this.root = root;
		check_status();
		df = new DecimalFormat("#0.000");
	}

	public void print_detailed_iteration_info() {
		print_MLIP_iteration_node(root);
	}

	public void print_result() {
		StringBuilder str = new StringBuilder();
		str.append("Result:" + this.status + "\n");
		if (this.status.equals(MLIPTermination.OPTIMAL)) {
			str.append("Optimal Node:\nP" + this.optimal.getCurrent_iteration() + "\nOptimal Value:\n"
					+ df.format(this.optimal.getLp_result().getSolution().getValue())
					+ "\nValue of Decision Variable:\n{");

			int length = this.optimal.getLp_result().getSolution().getPoint().length;
			for (int i = 0; i < length; i++) {
				if (i < length - 1)
					str.append(df.format(this.optimal.getLp_result().getSolution().getPoint()[i]) + "; ");
				else
					str.append(df.format(this.optimal.getLp_result().getSolution().getPoint()[i]) + "}\n");
			}
		}
		System.out.println(str.toString());
	}

	public int get_MLIP_enumeration_depth() {
		return getNodeDepth(root);
	}

	public void print_MLIP_enumeration_tree() {
		print_MLIP_enumeration_tree_node(root, 0);
	}

	private void check_status() {
		if (root == null)
			return;
		this.status = MLIPTermination.INFEASIBLE;
		Queue<MLIPIterationNode> queue = new LinkedList<MLIPIterationNode>();
		queue.add(root);
		while (!queue.isEmpty()) {
			MLIPIterationNode node = queue.poll();
			if (node.getTermination().equals(MLIPTermination.EXCEED)
					|| node.getTermination().equals(MLIPTermination.UNBOUNDED)) {
				this.status = node.getTermination();
				return;
			}

			if (node.getTermination().equals(MLIPTermination.OPTIMAL)) {
				this.status = node.getTermination();
				this.optimal = node;
				return;
			}
			if (node.getLeft_child() != null)
				queue.add(node.getLeft_child());
			if (node.getRight_child() != null)
				queue.add(node.getRight_child());
		}
	}

	private void print_MLIP_enumeration_tree_node(MLIPIterationNode current, int space) {
		if (current == null)
			return;

		if (space > 0) {
			for (int i = 0; i < space * 3; i++) {
				System.out.print(" ");
			}
			System.out.print("|_");
		}
		if (current.getLp_result().getStatus() == LPOptimizationStatus.OPTIMAL)
			System.out.println("P" + current.getCurrent_iteration() + ":"
					+ df.format(current.getLp_result().getSolution().getValue()) + ", Satuts:"
					+ current.getTermination());
		else
			System.out.println("P" + current.getCurrent_iteration() + ":" + current.getLp_result().getStatus()
					+ ", Satuts:" + current.getTermination());
		space++;
		if (current.getLeft_child() != null)
			print_MLIP_enumeration_tree_node(current.getLeft_child(), space);
		if (current.getRight_child() != null)
			print_MLIP_enumeration_tree_node(current.getRight_child(), space);

	}

	private void print_MLIP_iteration_node(MLIPIterationNode current) {
		if (current != null)
			System.out.println(current.toString());
		if (current.getLeft_child() != null)
			print_MLIP_iteration_node(current.getLeft_child());
		if (current.getRight_child() != null)
			print_MLIP_iteration_node(current.getRight_child());

	}

	private int getNodeDepth(MLIPIterationNode current) {
		if (current == null)
			return 0;
		else {
			int left_depth = getNodeDepth(current.getLeft_child());
			int right_depth = getNodeDepth(current.getRight_child());
			return Math.max(left_depth, right_depth) + 1;
		}
	}

	public MLIPIterationNode getRoot() {
		return root;
	}

	public MLIPTermination getStatus() {
		return status;
	}

	public MLIPIterationNode getOptimal() {
		return optimal;
	}
}
