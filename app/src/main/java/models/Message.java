    package models;

    public class Message {

        public String time;
        public boolean isReceived;  // true if received, false if sent
        private int id;
        private String created;
        private String content;

        public Message( int id, String created, String content) {

            this.id = id;
            this.created = created;
            this.content = content;
        }

        public Message(String text, String time, boolean isReceived) {
            this.content = text;
            this.time = time;
            this.isReceived = isReceived;
        }
        public int getId() {
            return id;
        }

        public String getCreated() {
            return created;
        }

        public String getContent() {
            return content;
        }



    }
