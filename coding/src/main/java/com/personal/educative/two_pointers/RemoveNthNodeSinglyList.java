package com.personal.educative.two_pointers;

import com.personal.educative.ListNode;

public class RemoveNthNodeSinglyList {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // Set up two pointers
        ListNode first = dummy;
        ListNode second = dummy;

        // Advance the first pointer n+1 steps ahead
        for (int i = 0; i <= n; i++) {
            first = first.next;
        }

        // Move both pointers until first reaches the end
        while (first != null) {
            first = first.next;
            second = second.next;
        }

        // Remove the target node
        second.next = second.next.next;

        // Return the new head (in case original head was removed)
        return dummy.next;
    }
}
