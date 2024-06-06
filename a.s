    .globl foo
foo:
    enter $(8 * 3), $0
    movq %rdi, -8(%rbp)
    reached nopInst
    reached nopInst
    reached copyInst
    3
    movq $0, %r10
    reached BinaryOperator
    reached returnInst
    leave
    ret
    .globl main
main:
    enter $(8 * 1), $0
    reached nopInst
    reached nopInst
    leave
    ret
