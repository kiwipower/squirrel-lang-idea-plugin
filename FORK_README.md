## Sam's Fork on the Squirrel Idea plugin

### Local installation
- Uninstall the Squirrel ide plugin
- Download the latest Squirrel-<version>.zip from [teamcity](https://teamcity.kiwipowered.com/viewLog.html?buildId=lastSuccessful&buildTypeId=FruitFirmware_SquirrelIdeaPlugin&tab=artifacts)
- Unzip in <IDEA_INSTALL_DIRECTORY>/plugins


### Features
- Control+b for 
    - variables
    - constants
    - functions
    - methods
    - class members
    - enum constants
    
### Build Locally

```bash
 ./build.sh 
```
Produces the zip under build/distribution

For developing in Intellij, just do file->new->project->Intellij Plugin Project and point to the directory where this project is checked out. Don't try to get gradle to generate the idea files for you (or link the gradle build to the project).

To re-generate the gen src from intellij click Control+Shift+g from the Squirrel.bnf file. 

There are a few tests from the original plugin, but nothing new. The SamplesTest is useful for seeing what AST is produced from some squirrel code.

### Known bugs

- For performance the files and element->resolution are cached. New files/ new usages will need a restart to start appearing. I'm happy with this trade off for the moment: being quick enough to browse is more important that being 
- The synthetic semi colon detection is broken in some cases. Add semi-colons to includes and local variables if Intellij shows red. It sometimes stops the references from being resolved.
- Inline json is not supported.    
      