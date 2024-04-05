package crux;

final class Authors {
  // TODO: Add author information.
  static final Author[] all = {new Author("Chan Young Ji", "24982109", "jicy"),
                               new Author("Venkata Neti", "72723519", "vneti")};
}


final class Author {
  final String name;
  final String studentId;
  final String uciNetId;

  Author(String name, String studentId, String uciNetId) {
    this.name = name;
    this.studentId = studentId;
    this.uciNetId = uciNetId;
  }
}
