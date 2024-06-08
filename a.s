    .globl fib
fib:
    enter $(8 * 12), $0
    movq %rdi, -8(%rbp)
    /* $t1 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -16(%rbp)
    /* $t2 = $a0 <= $t1 */
    movq $0, %rax
    movq $1, %r10
    movq -8(%rbp), %r11
    cmp -16(%rbp) , %r11
    cmovle %r10, %rax
    movq %rax, -24(%rbp)
    /* jump $t2 */
    movq -24(%rbp), %r10
    cmp $1, %r10
    je L1
L2:
    /* $t4 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -32(%rbp)
    /* $t5 = $a0 - $t4 */
    movq -8(%rbp), %r10
    subq -32(%rbp), %r10
    movq %r10, -40(%rbp)
    /* $t6 = call Symbol(fib:func(TypeList(int)):int) ($t5) */
    movq -40(%rbp), %rdi
    call fib
    movq %rax, -48(%rbp)
    /* $t7 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -56(%rbp)
    /* $t8 = $a0 - $t7 */
    movq -8(%rbp), %r10
    subq -56(%rbp), %r10
    movq %r10, -64(%rbp)
    /* $t9 = call Symbol(fib:func(TypeList(int)):int) ($t8) */
    movq -64(%rbp), %rdi
    call fib
    movq %rax, -72(%rbp)
    /* $t10 = $t6 + $t9 */
    movq -48(%rbp), %r10
    addq -72(%rbp), %r10
    movq %r10, -80(%rbp)
    /* return $t10 */
    movq -80(%rbp), %rax
    leave
    ret
    leave
    ret
L1:
    /* $t3 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -88(%rbp)
    /* return $t3 */
    movq -88(%rbp), %rax
    leave
    ret
    jmp L2
    .globl main
main:
    enter $(8 * 2), $0
    /* $t0 = 5 */
    /* CopyInst */
    movq $5, %r10
    movq %r10, -96(%rbp)
    /* $t1 = call Symbol(fib:func(TypeList(int)):int) ($t0) */
    movq -96(%rbp), %rdi
    call fib
    movq %rax, -104(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t1) */
    movq -104(%rbp), %rdi
    call printInt
    movq %rax, -112(%rbp)
    leave
    ret
