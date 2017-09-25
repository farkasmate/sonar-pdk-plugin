# test::test_type
#
# A description of what this defined type does
#
# @summary A short summary of the purpose of this defined type.
#
# @example
#   test::test_type { 'namevar': }
define test::test_type(
  String $foo,
) {
  notice $foo
}
