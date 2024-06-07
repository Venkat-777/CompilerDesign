    .globl main
main:
    enter $(8 * 2), $0
    /* nop */
    /* nop */
    /* $t0 = true */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* call Symbol(printBool:func(TypeList(bool)):void) ($t0) */
    movq -8(%rbp), %rdi
    call printBool
    /* nop */
    /* $t1 = false */
    movq $0, %r10
    movq %r10, -16(%rbp)
    /* call Symbol(printBool:func(TypeList(bool)):void) ($t1) */
    movq -16(%rbp), %rdi
    call printBool
    leave
    ret
