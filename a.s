    .globl main
main:
    enter $(8 * 3), $0
    /* nop */
    /* nop */
    /* $t0 = 1 */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* $t1 = 1 */
    movq $1, %r10
    movq %r10, -16(%rbp)
    /* $t2 = $t0 + $t1 */
    movq -0(), %r10
    addq 
    /* call Symbol(printInt:func(TypeList(int)):void) ($t2) */
    movq -16(%rbp), %rdi
    call printInt
    leave
    ret
