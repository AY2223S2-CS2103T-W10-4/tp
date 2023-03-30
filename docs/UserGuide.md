---
layout: page
title: User Guide
---

## INTRODUCTION

Tutee managing system (TMS) is a **desktop application designed for private tutors managing students, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). TMS utilizes your fast typing ability to execute your management tasks faster than traditional GUI apps.

# TABLE OF CONTENT

[FEATURES](#features)
1. [Add student](#add)
2. [Delete student](#delete)
3. [List student](#list)
4. [Local save](#save)
5. [Local load](#load)
6. [Exit program](#exit)

# FEATURES <a name="features"></a>

## Add student <a name="add"></a>

Adds a student to the managing system.

Format: ```add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS s/SUBJECT sch/SCHEDULE st/START TIME et/END TIME```
Subject supported: {`Math`, `Physics`, `English`}
Schedule supported: {`monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`, `sunday`}

Examples:

* ```add n/John Doe p/98765432 e/johnd@example.com a/block 224 s/Math sch/monday st/09:30 et/11:30```

## Delete student <a name="delete"></a>

Removes a student from the managing system.

Format: ```delete INDEX```

Examples:

* ```delete 1```


## List students <a name="list"></a>

Lists students in the managing system.

Format: ```list```

Examples:

* ```list``` 

## Filter students by any fields <a name='filter'></a>

Filter and list students whose fields contain any of the given keywords.

Format: ```filter [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [sch/SCHEDULE] [st/START TIME] [et/END TIME] [t/TAG]```

- Filter is case-insensitive. e.g `n/yeoh` will match `Yeoh`
- At least one of the optional fields must be provided.
- The order of the keywords does not matter. e.g. `n/Yeoh Alex` will match `Alex Yeoh`
- All fields can be searched and filtered.
- Only full words will be matched e.g. `n/Yeo` will not match `Yeoh`
- When only one field is specified, tutees matching at least one keyword will be returned(i.e. `OR` search). e.g. `a/blk street` will return `Blk 313`, `street 29`
- When more than one field is specified, only tutees whose keywords matching all fields provided will be returned
e.g. `filter s/math sch/monday` will only return tutees whose lessons are on `monday` AND subject `math`

Examples:
- `filter sch/tuesday` returns tutees whose lessons are on tuesday.
- `filter a/clementi s/math` returns tutees whose address are in `clementi` and being tutored `math` subject.
![img.png](images/filterExampleResult.png)

## Local save <a name="save"></a>

Saves the current state of the program on the hard disk upon exit.

Done automatically.

## Local load <a name="load"></a>

Loads the saved state of the program (if there is any) on the hard disk.

Creates an empty file if there is none.

Done automatically.

## Exit program <a name="exit"></a>

Exit the program.

Format: ```exit```

