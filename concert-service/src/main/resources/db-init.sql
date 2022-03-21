// Data in this class is required by the unit tests. DO NOT MODIFY THIS FILE.
// Users
INSERT INTO USERS (ID, USERNAME, PASSWORD, VERSION) VALUES (1, 'testuser', 'pa55word', 1);
INSERT INTO USERS (ID, USERNAME, PASSWORD, VERSION) VALUES (2, 'testuser2', 'pa55word', 1);


// Performers
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (1, 'Pentatonix', 'performers/ptx.jpg', 'Acappella', 'Pentatonix (abbreviated PTX) is an American a cappella group from Arlington, Texas, consisting of vocalists Scott Hoying, Mitch Grassi, Kirstin Maldonado, Kevin Olusola, and Matt Sallee. Characterized by their pop-style arrangements with vocal harmonies, basslines, riffing, percussion, and beatboxing, they produce cover versions of modern pop works or Christmas songs, sometimes in the form of medleys, along with original material.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (2, 'Fleetwood Mac', 'performers/fleetwoodmac.jpg', 'SoftRock', 'Fleetwood Mac are a British-American rock band, formed in London in 1967. They have sold more than 120 million records worldwide, making them one of the world''s best-selling bands. In 1998, select members of Fleetwood Mac were inducted into the Rock and Roll Hall of Fame and received the Brit Award for Outstanding Contribution to Music.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (3, 'Bastille', 'performers/bastille.jpg', 'Pop', 'Bastille is a British band formed in 2010. The group began as a solo project by lead vocalist Dan Smith, but later expanded to include keyboardist Kyle Simmons, bassist and guitarist Will Farquarson and drummer Chris "Woody" Wood. The name of the band derives from Bastille Day, which is celebrated on 14 July, the date of Smith''s birthday. Smith has said that the band was initially going to be called Daniel in the Den before they settled on Bastille. The name was later used for the 10th track on their album Bad Blood instead.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (4, 'Hugh Jackman', 'performers/hughjackman.jpg', 'Theatre', 'Hugh Michael Jackman AC (born 12 October 1968) is an Australian actor, singer, and producer. He is best known for playing Wolverine in the X-Men film series from 2000 to 2018, a role for which he holds the Guinness World Record for "longest career as a live-action Marvel superhero". Jackman is also recognised for his lead roles in films such as the romantic comedy Kate & Leopold (2001), the action film Van Helsing (2004), the drama The Prestige (2006), the film version of Les Miserables (2012), and the musical The Greatest Showman (2017), for which he received a Grammy Award for Best Soundtrack Album. For playing Jean Valjean in Les Miserables, he was nominated for the Academy Award for Best Actor and won the Golden Globe Award for Best Actor - Motion Picture Musical or Comedy.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (5, 'Keala Settle', 'performers/kealasettle.jpg', 'Theatre', 'Keala Joan Settle (born November 5, 1975) is an American actress and singer. Settle originated the role of Norma Valverde in Hands on a Hardbody, which ran on Broadway in 2013, and was nominated for the Outer Critics Circle Award, Drama Desk Award, and Tony Award for Best Featured Actress in a Musical. In 2017, she portrayed Lettie Lutz, a bearded lady, in the musical film The Greatest Showman. The song "This Is Me" from the film, principally sung by Settle, won the 2018 Golden Globe Award for Best Original Song, and was nominated for the Academy Award for Best Original Song.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (6, 'KISS', 'performers/kiss.jpg', 'Rock', 'Kiss is an American rock band formed in New York City in January 1973 by Paul Stanley, Gene Simmons, Peter Criss, and Ace Frehley. Well known for its members'' face paint and stage outfits, the group rose to prominence in the mid-to-late 1970s with their elaborate live performances, which featured fire breathing, blood-spitting, smoking guitars, shooting rockets, levitating drum kits, and pyrotechnics.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (7, 'Khalid', 'performers/khalid.jpg', 'RhythmAndBlues', 'Khalid Donnel Robinson (born February 11, 1998) is an American singer and songwriter. He signed with Right Hand Music Group and RCA Records. His debut single, "Location", was released in July 2016 and peaked at number 16 on the US Billboard Hot 100 chart and was later certified quadruple platinum by the Recording Industry Association of America (RIAA). His debut studio album, American Teen, was released on March 3, 2017.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (8, 'Little Mix', 'performers/littlemix.jpg', 'Pop', 'Little Mix are a British girl group formed in 2011 during the eighth series of the UK version of The X Factor. They were the first group to win the competition, and following their victory, they signed with Simon Cowell''s record label Syco Music and released a cover of Damien Rice''s "Cannonball" as their winner''s single. The members are Jade Thirlwall, Perrie Edwards, Leigh-Anne Pinnock, and Jesy Nelson.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (9, 'Robinson', 'performers/robinson.jpg', 'Pop', 'Anna Robinson, better known by the mononym Robinson, is a New Zealand singer-songwriter and musician. She is from the city of Nelson. Robinson released her first single "Don''t You Forget About Me" in May 2017. In February 2018, Robinson signed to Sony Music Australia and Ministry of Sound. Her song "Nothing to Regret" reached the top 40 in Australia and New Zealand in 2018. She is an opening act for the British girl group, Little Mix, on the LM5: The Tour.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (10, 'Shawn Mendes', 'performers/shawnmendes.jpg', 'Pop', 'Shawn Peter Raul Mendes (born August 8, 1998) is a Canadian singer, songwriter and model. He gained a following in 2013, posting song covers on the video-sharing application Vine. The following year, he caught the attention of artist manager Andrew Gertler and Island Records A&R Ziggy Chareton, which led to him signing a deal with the record label. He has since released three studio albums, headlined three world tours, and received several awards.');
INSERT INTO PERFORMERS (ID, NAME, IMAGE_NAME, GENRE, BLURB) VALUES (11, 'Ruel', 'performers/ruel.jpg', 'Pop', 'Ruel Vincent Van Dijk (born 29 October 2002), known mononymously as Ruel (pronounced "Rool", after the Dutch name "Roelof"), is an Australian singer-songwriter from Sydney, best known for his singles "Don''t Tell Me" and "Younger". At the ARIA Music Awards of 2018 he won the award for Breakthrough Artist for his single "Dazed & Confused".');


// Concerts
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (1, 'PTX: The World Tour', 'concerts/ptx.jpg', 'Three-time Grammy Award-winning and US multi-platinum-selling group, PENTATONIX, bring their phenomenal vocal talents to New Zealand this summer in what will be their second only visit.');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (2, 'Fleetwood Mac', 'concerts/fleetwoodmac.jpg', 'Legendary, GRAMMY-award winning band Fleetwood Mac are heading back to Auckland for four shows this September (Thursday 12 September, Saturday 14 September, Monday 16 September, and Thursday 19 September).');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (3, 'Bastille: Doom Days Tour', 'concerts/bastille.jpg', 'British pop superstars Bastille return to New Zealand at the beginning of next year on the Doom Days Tour, Part 2. Visiting Auckland on Thursday 23 January 2020, this tour will be fans'' first chance to hear the band''s third studio album Doom Days performed live.');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (4, 'Hugh Jackman: The Man. The Music. The Show.', 'concerts/hughjackman.jpg', 'Academy Award nominated, Golden Globe, Grammy and Tony Award-winning performer, Hugh Jackman, is bringing his The Man. The Music. The Show. WORLD TOUR to Auckland this September. The tour will see Jackman perform hit songs from from Broadway and film.');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (5, 'KISS: End of the Road World Tour', 'concerts/kiss.jpg', 'After an epic and storied 45-year career that launched an era of rock n roll legends, KISS announced that they will launch their final tour ever in 2019, appropriately named END OF THE ROAD.');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (6, 'Khalid: Free Spirit Tour', 'concerts/khalid.jpg', '21-year old superstar Khalid is heading back to New Zealand shores for his Free Spirit Tour. There''s no better time for fans down under to experience the El Paso singer''s magic.');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (7, 'Little Mix: LM5 Tour', 'concerts/littlemix.jpg', 'The world''s biggest girl band, Little Mix have added an Auckland show to their five date Australian "LM5 - The Tour" this December.');
INSERT INTO CONCERTS (ID, TITLE, IMAGE_NAME, BLURB) VALUES (8, 'Shawn Mendes, with special guest Ruel', 'concerts/shawnmendes.jpg', 'Shawn Mendes will bring his wildly-anticipated world tour to New Zealand in 2019, touring in support of his new self-titled third album.');


// Concert dates
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (1, '2020-02-15 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (2, '2019-09-12 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (2, '2019-09-14 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (2, '2019-09-16 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (2, '2019-09-19 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (3, '2020-01-23 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (4, '2019-09-06 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (4, '2019-09-07 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (5, '2019-12-03 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (6, '2019-11-20 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (6, '2019-11-21 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (7, '2019-12-19 20:00:00');
INSERT INTO CONCERT_DATES (CONCERT_ID, DATE) VALUES (8, '2019-11-09 20:00:00');


// Concert performers
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (1, 1);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (2, 2);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (3, 3);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (4, 4);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (4, 5);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (5, 6);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (6, 7);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (7, 8);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (7, 9);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (8, 10);
INSERT INTO CONCERT_PERFORMER (CONCERT_ID, PERFORMER_ID) VALUES (8, 11);