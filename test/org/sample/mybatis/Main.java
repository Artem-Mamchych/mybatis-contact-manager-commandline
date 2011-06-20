package org.sample.mybatis;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.sample.beans.Contact;
import org.sample.mybatis.mappers.ContactMapper;

public class Main {
    private SqlSession session = null;
    private Logger log = Logger.getLogger(getClass().getName());

    public Main() {
        super();
    }

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        try {
            log.info("Program started");
            String aResource = "iBatisConfig.xml";
            Reader reader = Resources.getResourceAsReader(aResource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            log.info(sessionFactory.getConfiguration().getEnvironment().getDataSource().toString());
            session = sessionFactory.openSession();
            ContactMapper mapper = session.getMapper(ContactMapper.class);
            List<Contact> contacts = mapper.selectAll();

            if (contacts.size() == 0) {
                Contact c = new Contact();
                c.setFirstName("FirstName");
                c.setLastName("LastName");
                c.setPhone(session.getClass().toString());
                c.setEmail("Email");
                log.info("Inserting: " + c);
                mapper.insert(c);
            }

            log.info("List<Contact> size=" + contacts.size());
            log.info("List<Contact> content:");
            for (Contact contact : contacts) {
                log.info("Contact: " + contact);
            }
        } catch (Exception e) {
            log.error("Exception caught: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
